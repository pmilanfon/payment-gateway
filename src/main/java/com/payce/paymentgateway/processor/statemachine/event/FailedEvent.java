package com.payce.paymentgateway.processor.statemachine.event;

import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;

public final class FailedEvent extends GenericErrorEvent {

    public FailedEvent(String reference, ErrorMessage errorMessage) {
        super(StateMachineEvent.FAIL_EVENT, reference, errorMessage);
    }
}
