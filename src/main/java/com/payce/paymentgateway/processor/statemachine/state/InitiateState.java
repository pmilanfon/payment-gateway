package com.payce.paymentgateway.processor.statemachine.state;


import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InitiateState extends BaseState {

    public InitiateState(DepositStorageService depositStorageService) {
        super(depositStorageService);
    }

    @Override
    public Event execute(String reference) {
        log.info("Transaction is Initiated");
        return new GenericEvent(StateMachineEvent.INITIATION_SUCCESS_EVENT, reference);
    }

    @Override
    public State getStateType() {
        return State.INITIATE;
    }
}
