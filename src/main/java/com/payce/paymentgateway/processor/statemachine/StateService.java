package com.payce.paymentgateway.processor.statemachine;

import com.payce.paymentgateway.common.entity.StateTransitionEntity;
import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.common.repo.StateTransitionRepository;
import com.payce.paymentgateway.processor.statemachine.configuration.StateTransition;
import com.payce.paymentgateway.processor.statemachine.event.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class StateService {
    private static final ErrorMessage NO_DISPLAY_MESSAGE = null;

    private final DepositRepository depositRequestRepository;
    private final StateTransitionRepository stateTransitionRepository;

    @Timed(MetricsNames.METHOD_CALLS)
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

        Date now = new Date();

        Optional<DepositRequestEntity> depositRef =
                depositRequestRepository.findById(reference.getReference());
        if (depositRef.isEmpty()) {
            throw new IllegalArgumentException(
                    "Could not find deposit request for reference=" + reference);
        }
        DepositRequestEntity depositRequestEntity = depositRef.get();
        // update current state
        depositRequestEntity.setStateUpdate(now);
        depositRequestEntity.setLatestRetry(now);
        depositRequestEntity.setUpdated(now);
        depositRequestEntity.setCurrentState(
                stateTransition.to().getStateType().name());

        if (errorMessage != null) {
            depositRequestEntity.setMessage(errorMessage.messageToAgents());
            depositRequestEntity.setTranslationKey(errorMessage.messageKeyForCustomers());
            depositRequestEntity.setErrorType(errorMessage.errorType());
            depositRequestEntity.setProviderErrorCode(errorMessage.providerCode());
            depositRequestEntity.setProviderErrorMessage(errorMessage.providerMessage());
            depositRequestEntity.setErrorSubtype(errorMessage.errorSubType());
        }
        depositRequestRepository.save(depositRequestEntity);

        // save state transition to database
        StateTransitionEntity stateTransitionEntity = new StateTransitionEntity();
        stateTransitionEntity.setDepositRequest(depositRequestEntity);
        stateTransitionEntity.setFromState(
                stateTransition.from().getStateType().name());
        stateTransitionEntity.setEvent(stateTransition.eventType().name());
        stateTransitionEntity.setToState(stateTransition.to().getStateType().name());
        stateTransitionEntity.setCreated(now);
        stateTransitionRepository.save(stateTransitionEntity);
    }
}
