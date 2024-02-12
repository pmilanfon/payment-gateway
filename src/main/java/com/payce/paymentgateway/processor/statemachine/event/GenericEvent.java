package com.payce.paymentgateway.processor.statemachine.event;


import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;

public final class GenericEvent extends Event {

    public GenericEvent(StateMachineEvent eventName, String requestReference) {
        super (eventName, requestReference);
    }
}
