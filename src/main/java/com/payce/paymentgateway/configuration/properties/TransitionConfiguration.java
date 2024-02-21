package com.payce.paymentgateway.configuration.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.util.List;

@ConfigurationProperties(prefix = "state-configuration")
public class TransitionConfiguration {

    private List<Transition> transitions;

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public record Transition(String event, String fromState, String toState) {

        public Transition {
            Assert.notNull(event, "Event type cannot be null");
            Assert.notNull(fromState, "fromState cannot be null");
            Assert.notNull(toState, "toState cannot be null");
        }
    }
}