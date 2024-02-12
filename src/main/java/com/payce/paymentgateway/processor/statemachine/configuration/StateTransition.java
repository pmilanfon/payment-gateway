package com.payce.paymentgateway.processor.statemachine.configuration;

import com.payce.paymentgateway.processor.statemachine.state.BaseState;
import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public record StateTransition(BaseState from, StateMachineEvent eventType, BaseState to) {
}
