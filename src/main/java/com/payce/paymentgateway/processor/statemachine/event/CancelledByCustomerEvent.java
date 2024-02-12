package com.payce.paymentgateway.processor.statemachine.event;

import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;

public class CancelledByCustomerEvent extends GenericErrorEvent {

    public CancelledByCustomerEvent(String reference, ErrorMessage errorMessage) {
        super(StateMachineEvent.CANCELLED_BY_CUSTOMER, reference, errorMessage );
    }
}
