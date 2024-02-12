package com.payce.paymentgateway.processor.statemachine.state;

import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

@AllArgsConstructor
public abstract class BaseState {
    private final State stateEnum;
    private final DepositRepository depositRepository;

    public abstract Event execute(String reference);

    protected final DepositDto getDepositRequest(String reference) {
        final DepositDto depositRequest = depositRepository.findByReference(reference).toDto();
        Assert.notNull(depositRequest, "deposit request must exist. Ref: " + reference);
        return depositRequest;
    }

    public boolean isFinal() {
        return stateEnum.isFinal;
    }
    public State getStateEnum() {
        return stateEnum;
    }
    public String toString() {
        return stateEnum.toString();
    }
}
