package com.payce.paymentgateway.processor.statemachine;


import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.configuration.StateConfiguration;
import com.payce.paymentgateway.processor.statemachine.configuration.StateTransition;
import com.payce.paymentgateway.processor.statemachine.event.ErrorMessage;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.GenericErrorEvent;
import com.payce.paymentgateway.processor.statemachine.event.TriggerStateMachineEvent;
import com.payce.paymentgateway.processor.statemachine.lock.LockService;
import com.payce.paymentgateway.processor.statemachine.state.BaseState;
import com.payce.paymentgateway.processor.statemachine.state.State;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class StateMachine {
    private final StateService stateService;
    private final LockService lockService;
    private final StateConfiguration stateConfiguration;
    private final DepositStorageService depositStorageService;

    @Async
    public void onEventAsync(TriggerStateMachineEvent event) {
        onEvent(event);
    }

    public void onEvent(TriggerStateMachineEvent event) {
        log.info("State machine triggered for ref {} by {}", event.getReference(), event.getTriggeredFrom());
        lockService.performWithLock(event.getReference(), () -> perform(event));
    }

    //todo why not fetch deposit before this method, but in each state?
    private void perform(Event event) {
        try {
            DepositDto transactionRequest = depositStorageService
                    .getRequest(event.getReference());

            addReferenceToMdc(transactionRequest);
            BaseState currentState = stateConfiguration.getState(transactionRequest.getCurrentState());

            log.info("Event State: {}, current state: {}", event.getEventEnum(), currentState);

            handleEvent(event, currentState);
        } finally {
            MDC.clear();
        }
    }

    private void handleEvent(Event event, BaseState currentState) {
        try {
            log.info("Executing state {} for event {}", currentState.getStateType(), event);
            Event returnedEvent = currentState.execute(event.getReference());

            if (returnedEvent != null) {
                BaseState nextState = stateConfiguration.getNext(currentState, returnedEvent);
                doStateTransition(
                        returnedEvent,
                        new StateTransition(currentState, returnedEvent.getEventEnum(), nextState));
                handleEvent(returnedEvent, nextState);
            }
        } catch (RuntimeException e) {
            log.error(
                    "Caught runtime exception - forced update of its latestRetry. "
                            + "Scheduler will retry according to normal configuration. {}",
                    ExceptionUtils.getStackTrace(e));
        } finally {
            depositStorageService.saveLatestRetry(event.getReference());
        }
    }

    private void doStateTransition(Event event, StateTransition stateTransition) {
        if (event instanceof GenericErrorEvent genericErrorEvent) {
            ErrorMessage errorMessage = genericErrorEvent.getErrorMessage();
            stateService.doStateTransitionInDatabase(event.getReference(), stateTransition, errorMessage);
        } else {
            stateService.doStateTransitionInDatabase(event.getReference(), stateTransition);
        }
    }

    private static void addReferenceToMdc(DepositDto request) {
        MDC.put("txRef", request.getReference());
    }

    public BaseState getState(State currentState) {
        return stateConfiguration.getState(currentState);
    }
}
