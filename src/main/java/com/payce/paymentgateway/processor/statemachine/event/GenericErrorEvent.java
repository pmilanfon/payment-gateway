package com.payce.paymentgateway.processor.statemachine.event;

import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;
import org.apache.commons.lang3.Validate;

public class GenericErrorEvent extends Event {

    private final ErrorMessage errorMessage;

    public GenericErrorEvent(StateMachineEvent name, String reference, ErrorMessage errorMessage) {
        super(name, reference);
        Validate.notNull(errorMessage, "Exception must not be null");
        this.errorMessage = errorMessage;

    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
