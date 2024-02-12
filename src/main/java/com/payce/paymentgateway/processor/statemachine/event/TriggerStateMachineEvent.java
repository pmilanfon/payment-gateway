package com.payce.paymentgateway.processor.statemachine.event;

import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;
import jakarta.validation.constraints.NotBlank;
import lombok.ToString;

@ToString
public class TriggerStateMachineEvent extends Event {

    @NotBlank
    private final String triggeredFrom;

    /**
     * @param reference deposit reference to use when starting statemachine
     * @param triggeredFrom description of "who" or "what" triggered the state machine. Used for logging purposes
     */
    public TriggerStateMachineEvent(String reference, String triggeredFrom) {
        super(StateMachineEvent.TRIGGER_STATE_MACHINE, reference);
        this.triggeredFrom = triggeredFrom;
    }
}
