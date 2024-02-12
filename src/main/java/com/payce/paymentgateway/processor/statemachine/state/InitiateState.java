package com.payce.paymentgateway.processor.statemachine.state;


import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitiateState extends BaseState {

    public InitiateState(State stateEnum, DepositRepository depositRepository) {
        super(stateEnum, depositRepository);
    }

    @Override
    public Event execute(String reference) {
        log.info("Transaction is Initiated");
        return new GenericEvent(StateMachineEvent.INITIATION_SUCCESS_EVENT, reference);
    }
}
