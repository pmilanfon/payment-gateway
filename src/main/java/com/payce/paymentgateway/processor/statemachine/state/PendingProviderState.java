package com.payce.paymentgateway.processor.statemachine.state;

import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PendingProviderState extends BaseState {

    public PendingProviderState( DepositStorageService depositStorageService) {
        super(depositStorageService);
    }

    @Override
    public Event execute(String reference) {
        log.info("Execute state");
        return new GenericEvent(StateMachineEvent.FINANCIAL_TRANSACTION_POSTED_EVENT, reference);
    }

    @Override
    public State getStateType() {
        return State.PENDING_PROVIDER;
    }
}
