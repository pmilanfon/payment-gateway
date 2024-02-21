package com.payce.paymentgateway.processor.statemachine.configuration;

import com.payce.paymentgateway.processor.statemachine.state.BaseState;
import com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent;
import lombok.Builder;

@Builder
public record StateTransition(BaseState from, StateMachineEvent event, BaseState to) {
}
