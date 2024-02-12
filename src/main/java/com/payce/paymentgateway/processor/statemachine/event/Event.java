package com.payce.paymentgateway.processor.statemachine.event;

import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public abstract class Event {
    @NotNull
    private final StateMachineEvent eventEnum;
    @NotNull
    private final String reference;
}
