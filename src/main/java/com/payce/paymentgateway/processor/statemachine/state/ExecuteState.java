package com.payce.paymentgateway.processor.statemachine.state;


import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExecuteState extends BaseState {

    public ExecuteState(State stateEnum, DepositStorageService depositStorageService) {
        super(stateEnum, depositStorageService);
    }

    @Override
    public Event execute(String reference) {
        log.info("Execute state");
        return new GenericEvent(StateMachineEvent.FINANCIAL_TRANSACTION_POSTED_EVENT, reference);
    }
}
