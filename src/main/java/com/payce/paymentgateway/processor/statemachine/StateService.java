package com.payce.paymentgateway.processor.statemachine;

import com.payce.paymentgateway.common.entity.DepositEntity;
import com.payce.paymentgateway.common.entity.StateTransitionEntity;
import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.common.repo.StateTransitionRepository;
import com.payce.paymentgateway.processor.statemachine.configuration.StateTransition;
import com.payce.paymentgateway.processor.statemachine.event.ErrorMessage;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Service
public class StateService {
    private static final ErrorMessage NO_DISPLAY_MESSAGE = null;

    private final DepositRepository depositRequestRepository;
    private final StateTransitionRepository stateTransitionRepository;

    @Timed
    @Transactional(propagation = Propagation.REQUIRED)
    public void doStateTransitionInDatabase(
            String reference, StateTransition stateTransition) {
        Validate.notNull(reference, "reference must be set");
        Validate.notNull(stateTransition, "stateTransition must be set");
        doStateTransitionInDatabase(reference, stateTransition, NO_DISPLAY_MESSAGE);
    }

    public void doStateTransitionInDatabase(
            String reference, StateTransition stateTransition, ErrorMessage errorMessage) {
        log.info(
                "Perform state transition in db,"
                        + " from state={}, to state={},"
                        + " deposit reference={}, with errorMessage={}",
                stateTransition.from().getStateType(),
                stateTransition.to().getStateType(),
                reference,
                errorMessage);

        LocalDateTime now = LocalDateTime.now();

        DepositEntity depositEntity =
                depositRequestRepository.findByReference(reference)
                        .setLatestRetry(now)
                        .setCurrentState(stateTransition.to().getStateType().name());

        if (errorMessage != null) {
            depositEntity
                    .setCustomerMessageKey(errorMessage.getCustomerMessageKey())
                    .setInternalMessageKey(errorMessage.getInternalMessageKey());
        }

        depositRequestRepository.save(depositEntity);

        // save state transition to database
        StateTransitionEntity stateTransitionEntity = new StateTransitionEntity()
                .setDepositRequest(depositEntity)
                .setFromState(stateTransition.from().getStateType().name())
                .setEvent(stateTransition.event().name())
                .setToState(stateTransition.to().getStateType().name())
                .setCreated(now);

        stateTransitionRepository.save(stateTransitionEntity);
    }
}
