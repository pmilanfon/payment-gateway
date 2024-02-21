//package com.payce.paymentgateway.processor.statemachine.job;
//
//import com.payce.paymentgateway.processor.statemachine.StateMachine;
//import com.payce.paymentgateway.processor.statemachine.StateService;
//import com.payce.paymentgateway.processor.statemachine.configuration.StateTransition;
//import com.payce.paymentgateway.processor.statemachine.event.ErrorMessage;
//import com.payce.paymentgateway.processor.statemachine.event.TriggerStateMachineEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//public class RetryOrFailStatesJob {
//
//    private static final Pageable PAGE_SIZE = ofSize(100);
//    private static final long MILLIS_PER_SECOND = 1000L;
//    private static final long SECONDS_PER_MINUTE = 60L;
//
//    private final RetryOrFailProperties properties;
//
//    private final StateMachine stateMachine;
//
//    private final DepositStorageService<?> depositStorageService;
//
//    private final StateService stateService;
//
//    public RetryOrFailStateJob(
//            RetryOrFailProperties properties,
//            StateMachine stateMachine,
//            DepositStorageService<?> depositStorageService,
//            StateService stateService) {
//        this.properties = properties;
//        this.stateMachine = stateMachine;
//        this.depositStorageService = depositStorageService;
//        this.stateService = stateService;
//    }
//
//    @Scheduled(
//            fixedDelayString = "${retry-job.fixed-delay:5}",
//            initialDelayString = "${retry-job.initial-delay:10}",
//            timeUnit = SECONDS)
//    public void execute() {
//        try {
//            clearMdc();
//
//            Instant start = Instant.now();
//            log.info("Running RetryOrFailStateJob");
//
//            properties.getStateRetryTimeLimit().entrySet().stream()
//                    .sorted(Entry.comparingByValue())
//                    .forEach(this::retryState);
//
//            log.info("RetryOrFailStateJob finished in {} seconds.", Duration.between(start, Instant.now()).toSeconds());
//
//        } finally {
//            clearMdc();
//        }
//    }
//
//    private void retryState(Entry<String, Long> retry) {
//        String state = retry.getKey();
//        Instant retryLimit = Instant.now().minus(retry.getValue(), ChronoUnit.SECONDS);
//
//        boolean fullPage = true;
//
//        while (fullPage) {
//            List<? extends TransactionRequest> transactionRequestsToRetry =
//                    depositStorageService.findByStateAndTimeLimits(state, Date.from(retryLimit), PAGE_SIZE);
//
//            log.info(
//                    "Running retry! [state={}, retryLimit={}, resultSize={}]",
//                    state,
//                    retryLimit,
//                    transactionRequestsToRetry.size());
//
//            fullPage = transactionRequestsToRetry.size() >= PAGE_SIZE.getPageSize();
//
//            transactionRequestsToRetry.forEach(this::retry);
//        }
//    }
//
//    private void retry(TransactionRequest request) {
//        RequestReference requestReference = request.getRequestReference();
//        log.info("Retrying state for ref={}, state={}", requestReference, request.getCurrentState());
//        try {
//            stateMachine.onEvent(new TriggerStateMachineEvent(requestReference, "RetryJob"));
//        } catch (Exception e) {
//            String msg = "Retry of state failed for %s. Will evaluate again. Exception thrown:".formatted(requestReference);
//            log.error(msg, e);
//        } finally {
//            MDC.clear();
//        }
//        moveToFailedIfAppropriate(requestReference);
//    }
//
//    private void moveToFailedIfAppropriate(RequestReference requestReference) {
//        try {
//            TransactionRequest request = depositStorageService
//                    .getRequest(requestReference)
//                    .orElseThrow(() -> new RuntimeException("Request not found!"));
//
//            if (shouldBeFailed(request)) {
//                moveToFailed(request);
//            } else if (shouldAlertLog(request)) {
//                alertLog(request);
//            }
//        } catch (Exception exception) {
//            log.error(
//                    "Failed to move to failed state for requestReference={}.", requestReference, exception);
//        }
//    }
//
//    private boolean shouldBeFailed(TransactionRequest request) {
//        StateType currentState = request.getCurrentState();
//        long timeSinceCreated = System.currentTimeMillis() - request.getCreated().getTime();
//
//        return !currentState.isFinal()
//                && isConfigured(currentState, properties.getStateFailTimeLimit())
//                && timeSinceCreated > Duration.ofSeconds(properties.getStateFailTimeLimit().get(currentState.name()))
//                .toMillis();
//    }
//
//    private void moveToFailed(TransactionRequest transactionRequest) {
//        StateType currentState = transactionRequest.getCurrentState();
//        RequestReference requestReference = transactionRequest.getRequestReference();
//
//        String messageToDb = doLoggingAndGetMessage(transactionRequest, currentState);
//
//        // Store transition to database directly without considering the state machine.
//        // As the state machine is not included here, we can create a state without logic
//        StateTransition forcedFailTransition =
//                new StateTransition(
//                        FailureEvents.FORCED_FAIL,
//                        stateMachine.getState(currentState),
//                        stateMachine.getState(FailureStates.HANDLE_FAILURE));
//
//        transactionRequest = transactionRequest.setMessage(messageToDb);
//
//        depositStorageService.saveMessage(requestReference, messageToDb);
//
//        String genericError = "paycashierclient.common.bank.%s.error.genericException"
//                .formatted(transactionRequest.getTransactionType().name().toLowerCase());
//
//        stateService.doStateTransitionInDatabase(
//                requestReference, forcedFailTransition, new ErrorMessage(messageToDb, genericError));
//    }
//
//    private String doLoggingAndGetMessage(TransactionRequest requestEntity, StateType currentState) {
//        String messageToDb = "RetryOrFailStatesJob moved the transaction to failed. No apparent reason.";
//        log.warn("From {}, moving ref={} to failed state", currentState, requestEntity.getRequestReference());
//        return messageToDb;
//    }
//
//    private boolean shouldAlertLog(TransactionRequest request) {
//        StateType state = request.getCurrentState();
//        long timeSinceUpdated = System.currentTimeMillis() - request.getStateUpdate().getTime();
//        return !state.isFinal()
//                && isConfigured(state, properties.getStateLogAlertTimeLimit())
//                && timeSinceUpdated > Duration.ofSeconds(properties.getStateLogAlertTimeLimit().get(state.name())).toMillis();
//    }
//
//    private void alertLog(final TransactionRequest request) {
//        StateType state = request.getCurrentState();
//        long timeSinceUpdated = System.currentTimeMillis() - request.getStateUpdate().getTime();
//        long minutes = timeSinceUpdated / MILLIS_PER_SECOND / SECONDS_PER_MINUTE;
//        Long alertLimit = properties.getStateLogAlertTimeLimit().get(state.name());
//
//        log.warn(
//                "Alert: transaction request with ref={} has been in state={} for minutes={} and limit is={}",
//                request.getRequestReference(), state, minutes, alertLimit);
//    }
//
//    /**
//     * Log if MDC is not cleared - in order to <b>detect</b> if we somewhere forgot to clear MDC. If we never se this -
//     * replace with simply <code>MDCUtil.clear();</code>
//     */
//    private void clearMdc() {
//        if (MDC.getCopyOfContextMap() != null && !MDC.getCopyOfContextMap().isEmpty()) {
//            log.warn("MDC - should have been empty");
//        }
//        MDC.clear();
//    }
//
//    protected boolean isConfigured(StateType state, Map<String, Long> map) {
//        return map.containsKey(state.name()) && map.get(state.name()) >= 0L;
//    }
//}
