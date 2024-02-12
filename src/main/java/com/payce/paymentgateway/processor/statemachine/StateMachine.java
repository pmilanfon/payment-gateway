package com.payce.paymentgateway.processor.statemachine;


import com.payce.paymentgateway.common.repo.DepositRepository;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class StateMachine {
    private final DepositRepository depositRepository;
    private final StateService stateService;
    private final LockService lockService;
    private final StateConfiguration stateConfiguration;

    public void onEvent(TriggerStateMachineEvent event) {
        lockService.performWithLock(event.getReference(), () -> perform(event));
    }

    @Async
    public void onEventAsync(TriggerStateMachineEvent event) {
        onEvent(event);
    }

    private void perform(TriggerStateMachineEvent event) {
        try {
            TransactionRequest transactionRequest = depositStorageService
                    .getRequest(event.getReference())
                    .orElseThrow(() ->
                            new InvalidReferenceException("Could not find deposit request for reference: " + event.getReference()));

            addReferenceToMdc(transactionRequest);
            MachineState currentState = stateConfiguration.getMachineState(transactionRequest.getCurrentState());

            logger.info("Event State: {}, current state: {}", event.getEventType(), currentState);

            handleEvent(event, currentState);
        } finally {
            MDC.clear();
        }
    }

    private void handleEvent(Event event, MachineState currentState) {
        try {
            logger.info("Executing state {} for event {}", currentState.getStateType(), event);
            Event returnedEvent = currentState.execute(event.getReference());

            if (returnedEvent == null) {
                depositStorageService.saveLatestRetry(event.getReference());
            } else {
                MachineState nextState = stateConfiguration.getNext(currentState, returnedEvent.getEventType());
                doStateTransition(
                        returnedEvent,
                        new StateTransition(returnedEvent.getEventType(), currentState, nextState));
                handleEvent(returnedEvent, nextState);
            }
        } catch (RuntimeException e) {
            depositStorageService.saveLatestRetry(event.getReference());
            logger.error(
                    "Caught runtime exception - forced update of its latestRetry. "
                            + "Scheduler will retry according to normal configuration. {}",
                    ExceptionUtils.getStackTrace(e));
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

    private static void addReferenceToMdc(TransactionRequest request) {
        String key = request.getTransactionType() == DEPOSIT ? "depositRef" : "withdrawalRef";
        MDC.put(key, request.getRequestReference().getReference());
    }

    public MachineState getState(StateType stateType) {
        return stateConfiguration.getMachineState(stateType);
    }

    //todo why not fetch deposit before this method, but in each state?
    private void handleEvent(Event event, BaseState currentState) {
        try {
            log.info("Executing state {} for event {}", currentState.getStateEnum(), event);
            final Event returnedEvent = currentState.execute(event.getReference());

            if (returnedEvent != null) {
                final BaseState nextState = stateConfiguration.getNext(currentState, returnedEvent);
                doStateTransition(returnedEvent, new StateTransition(currentState, returnedEvent.getEventEnum(), nextState));
                handleEvent(returnedEvent, nextState);
            } else {
                //no returned event => mark as "retried" and then stop state machine processing.
                depositRepository.saveLatestRetry(event.getReference());
            }
        }
        catch (RuntimeException e) {
            depositRepository.saveLatestRetry(event.getReference());
            log.error("Caught runtime exception - forced update of its lastRetry. Scheduler will retry according to normal configured interval");
            throw e;
        }
    }

    private void doStateTransition(Event event, StateTransition stateTransition){
        if(event instanceof GenericErrorEvent){
            //Only set agent and customer messages upon fail event.
            final ErrorMessage errorMessage = ((GenericErrorEvent) event).getErrorMessage();
            stateService.doStateTransitionInDatabase(event.getReference(), stateTransition, errorMessage);
        } else {
            stateService.doStateTransitionInDatabase(event.getReference(), stateTransition);
        }
    }

    public BaseState getState(State currentState) {
        return stateConfiguration.getState(currentState);
    }
}
