package com.payce.paymentgateway.processor.statemachine.job;

import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.StateMachine;
import com.payce.paymentgateway.processor.statemachine.StateService;
import com.payce.paymentgateway.processor.statemachine.configuration.RetryOrFailProperties;
import com.payce.paymentgateway.processor.statemachine.configuration.StateTransition;
import com.payce.paymentgateway.processor.statemachine.event.ErrorMessage;
import com.payce.paymentgateway.processor.statemachine.event.TriggerStateMachineEvent;
import com.payce.paymentgateway.processor.statemachine.state.State;
import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.data.domain.Pageable.ofSize;


@Slf4j
@AllArgsConstructor
@Component
public class RetryOrFailStatesJob {

    private static final Pageable PAGE_SIZE = ofSize(100);
    private static final long MILLIS_PER_SECOND = 1000L;
    private static final long SECONDS_PER_MINUTE = 60L;

    private final RetryOrFailProperties properties;

    private final StateMachine stateMachine;

    private final DepositStorageService depositStorageService;

    private final StateService stateService;

    @Scheduled(
            fixedDelayString = "${retry-job.fixed-delay:5}",
            initialDelayString = "${retry-job.initial-delay:10}",
            timeUnit = SECONDS)
    public void execute() {
        try {
            clearMdc();

            LocalDateTime start = LocalDateTime.now();
            log.info("Running RetryOrFailStateJob");

            properties.getStateRetryTimeLimit().entrySet().stream()
                    .sorted(Entry.comparingByValue())
                    .forEach(this::retryState);

            log.info("RetryOrFailStateJob finished in {} seconds.", Duration.between(start, LocalDateTime.now()).toSeconds());

        } finally {
            clearMdc();
        }
    }

    private void retryState(Entry<String, Long> retry) {
        String state = retry.getKey();
        LocalDateTime retryLimit = LocalDateTime.now().minusSeconds(retry.getValue());

        boolean fullPage = true;

        while (fullPage) {
            List<DepositDto> depositsToRetry =
                    depositStorageService.findByStateAndTimeLimits(state, retryLimit, PAGE_SIZE);

            log.info(
                    "Running retry! [state={}, retryLimit={}, resultSize={}]",
                    state,
                    retryLimit,
                    depositsToRetry.size());

            fullPage = depositsToRetry.size() >= PAGE_SIZE.getPageSize();

            depositsToRetry.forEach(this::retry);
        }
    }

    private void retry(DepositDto depositDto) {
        String requestReference = depositDto.getReference();
        log.info("Retrying state for ref={}, state={}", requestReference, depositDto.getCurrentState());
        try {
            // things to consider
            // async call, one thread by default if not on java 21 so we do not benefit from it
            // can be moved to failed down bellow while state machine is handling event
            // exception we are trying to catch will not be propagated
            stateMachine.onEvent(new TriggerStateMachineEvent(requestReference, "RetryJob"));
        } catch (Exception e) {
            String msg = "Retry of state failed for %s. Will evaluate again. Exception thrown:".formatted(requestReference);
            log.error(msg, e);
        } finally {
            MDC.clear();
        }
        moveToFailedIfAppropriate(requestReference);
    }

    private void moveToFailedIfAppropriate(String requestReference) {
        try {
            DepositDto depositDto = depositStorageService
                    .getRequest(requestReference);

            if (shouldBeFailed(depositDto)) {
                moveToFailed(depositDto);
            } else if (shouldAlertLog(depositDto)) {
                alertLog(depositDto);
            }
        } catch (Exception exception) {
            log.error(
                    "Failed to move to failed state for requestReference={}.", requestReference, exception);
        }
    }

    private boolean shouldBeFailed(DepositDto depositDto) {
        State currentState = depositDto.getCurrentState();
        long timeSinceCreated = System.currentTimeMillis() - depositDto.getDepositDate().atZone(ZoneId.systemDefault()).toEpochSecond();

        return !currentState.isFinalState()
                && isConfigured(currentState, properties.getStateFailTimeLimit())
                && timeSinceCreated > Duration.ofSeconds(properties.getStateFailTimeLimit().get(currentState.name()))
                .toMillis();
    }

    private void moveToFailed(DepositDto depositDto) {
        State currentState = depositDto.getCurrentState();
        String requestReference = depositDto.getReference();

        String messageToDb = doLoggingAndGetMessage(depositDto, currentState);

        // Store transition to database directly without considering the state machine.
        // As the state machine is not included here, we can create a state without logic
        StateTransition forcedFailTransition =
                new StateTransition(
                        stateMachine.getState(currentState),
                        StateMachineEvent.FORCED_FAIL,
                        stateMachine.getState(State.HANDLE_FAILURE));

        String genericError = "payce.genericError";

        stateService.doStateTransitionInDatabase(
                requestReference, forcedFailTransition, new ErrorMessage(messageToDb, genericError));
    }

    private String doLoggingAndGetMessage(DepositDto depositDto, State currentState) {
        String messageToDb = "RetryOrFailStatesJob moved the transaction to failed. No apparent reason.";
        log.warn("From {}, moving ref={} to failed state", currentState, depositDto.getReference());
        return messageToDb;
    }

    private boolean shouldAlertLog(DepositDto depositDto) {
        State state = depositDto.getCurrentState();
        long timeSinceUpdated = System.currentTimeMillis() - depositDto.getStateUpdate().atZone(ZoneId.systemDefault()).toEpochSecond();
        return !state.isFinalState()
                && isConfigured(state, properties.getStateLogAlertTimeLimit())
                && timeSinceUpdated > Duration.ofSeconds(properties.getStateLogAlertTimeLimit().get(state.name())).toMillis();
    }

    private void alertLog(final DepositDto depositDto) {
        State state = depositDto.getCurrentState();
        long timeSinceUpdated = System.currentTimeMillis() - depositDto.getStateUpdate().atZone(ZoneId.systemDefault()).toEpochSecond();
        long minutes = timeSinceUpdated / MILLIS_PER_SECOND / SECONDS_PER_MINUTE;
        Long alertLimit = properties.getStateLogAlertTimeLimit().get(state.name());

        log.warn(
                "Alert: transaction request with ref={} has been in state={} for minutes={} and limit is={}",
                depositDto.getReference(), state, minutes, alertLimit);
    }

    /**
     * Log if MDC is not cleared - in order to <b>detect</b> if we somewhere forgot to clear MDC. If we never se this -
     * replace with simply <code>MDCUtil.clear();</code>
     */
    private void clearMdc() {
        if (MDC.getCopyOfContextMap() != null && !MDC.getCopyOfContextMap().isEmpty()) {
            log.warn("MDC - should have been empty");
        }
        MDC.clear();
    }

    protected boolean isConfigured(State state, Map<String, Long> map) {
        return map.containsKey(state.name()) && map.get(state.name()) >= 0L;
    }
}
