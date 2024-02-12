package com.payce.paymentgateway.processor.statemachine.state;


import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailedState extends BaseState {

    private static final Logger logger = LoggerFactory.getLogger(FailedState.class);

    public FailedState(State stateEnum, DepositRepository depositRepository) {
        super(stateEnum, depositRepository);
    }

    @Override
    public Event execute(String reference) {
        return null;
    }

}
