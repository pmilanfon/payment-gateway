package com.payce.paymentgateway.processor.statemachine.state;

import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import org.springframework.stereotype.Service;

@Service
public class DepositSuccessfulState extends BaseState {

    public DepositSuccessfulState(DepositStorageService depositStorageService) {
        super(depositStorageService);
    }

    @Override
    public Event execute(String reference) {
        return null;
    }

    @Override
    public State getStateType() {
        return State.DEPOSIT_SUCCESSFUL;
    }

}
