package com.payce.paymentgateway.processor.statemachine.state;

import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.event.Event;

public class DepositSuccessfulState extends BaseState {

    public DepositSuccessfulState(DepositStorageService depositStorageService) {
        super(State.DEPOSIT_SUCCESSFUL, depositStorageService);
    }

    @Override
    public Event execute(String reference) {
        return null;
    }

}
