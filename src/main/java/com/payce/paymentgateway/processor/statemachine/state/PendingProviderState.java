package com.payce.paymentgateway.processor.statemachine.state;

import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PendingProviderState extends BaseState {

    public PendingProviderState(State stateEnum, DepositRepository depositRepository) {
        super(stateEnum, depositRepository);
    }

    @Override
    public Event execute(String reference) {
        log.info("Execute state");
        return new GenericEvent(StateMachineEvent.FINANCIAL_TRANSACTION_POSTED_EVENT, reference);
    }
}
