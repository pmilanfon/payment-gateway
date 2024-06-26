package com.payce.paymentgateway.processor.statemachine.configuration;

import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.state.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.payce.paymentgateway.processor.statemachine.state.StateMachineEvent.*;

@Configuration
public class StateConfiguration {

    private final Logger logger = LoggerFactory.getLogger(StateConfiguration.class);

    private final Set<StateTransition> stateTransitions;

    private final BaseState initiate;
    private final BaseState validate;
    private final BaseState createOrder;
    private final BaseState pendingProvider;
    private final BaseState execute;
    private final BaseState depositSuccessful;
    private final BaseState failed;

    public StateConfiguration(InitiateState initiateState,
                              ValidateState validateState,
                              CreateOrderState createOrderState,
                              PendingAcquirerState pendingAcquirerState,
                              ExecuteState executeState,
                              DepositSuccessfulState depositSuccessfulState,
                              FailedState failedState) {

        initiate = initiateState;
        validate = validateState;
        createOrder = createOrderState;
        pendingProvider = pendingAcquirerState;
        execute = executeState;
        depositSuccessful = depositSuccessfulState;
        failed = failedState;

        this.stateTransitions = createStateTransitionSet();
    }

    private Set<StateTransition> createStateTransitionSet() {

        final Set<StateTransition> initStateTransitions = new HashSet<>();

        // Create available state transitions
        initStateTransitions.add(StateTransition.builder().from(initiate).event(INITIATION_SUCCESS_EVENT).to(validate).build());
        initStateTransitions.add(StateTransition.builder().from(validate).event(VALIDATION_SUCCESS_EVENT).to(createOrder).build());
        initStateTransitions.add(StateTransition.builder().from(createOrder).event(ORDER_CREATED_EVENT).to(pendingProvider).build());
        initStateTransitions.add(StateTransition.builder().from(pendingProvider).event(PROVIDER_NOTIFIED_OK_EVENT).to(execute).build());
        initStateTransitions.add(StateTransition.builder().from(pendingProvider).event(CANCELLED_BY_CUSTOMER).to(failed).build());
        initStateTransitions.add(StateTransition.builder().from(pendingProvider).event(EXPIRED_EVENT).to(failed).build());
        initStateTransitions.add(StateTransition.builder().from(pendingProvider).event(FAIL_EVENT).to(failed).build());
        initStateTransitions.add(StateTransition.builder().from(execute).event(FINANCIAL_TRANSACTION_POSTED_EVENT).to(depositSuccessful).build());

       Stream.of(initiate, validate, createOrder, pendingProvider, execute).
                filter(from -> !from.isFinal()).
                forEach( from -> initStateTransitions.add(StateTransition.builder().from(from).event(FAIL_EVENT).to(failed).build())
        );

        return Collections.unmodifiableSet(initStateTransitions);
    }

    public BaseState getState(State stateEnum) {
        final Set<BaseState> availableStates = getAllStates();
        for(final BaseState state :availableStates) {
            if (state.getStateType() == stateEnum) {
                return state;
            }
        }
        throw new IllegalStateException("Could not find state representation for input state: " + stateEnum);
    }

    private Set<BaseState> getAllStates(){
        final Set<BaseState> configuredStates = new HashSet<>();
        for(final StateTransition stateTransition : stateTransitions){
            configuredStates.add(stateTransition.from());
            configuredStates.add(stateTransition.to());
        }
        return configuredStates;
    }


    public BaseState getNext(BaseState from, Event event) {
        for (StateTransition st : stateTransitions) {
            if (st.from().getStateType()== (from.getStateType()) && st.event()==event.getEventEnum()) {
                if(from.isFinal()) {
                    logger.error("moving from final state {} to {}, event {}", from.getStateType(), st.to().getStateType(), event);
                }
                return st.to();
            }
        }
        throw new IllegalStateException(String.format("no state transition moving from state %s with event %s", from.getStateType(), event));
    }
}

