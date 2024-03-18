package com.payce.paymentgateway.processor.statemachine.state;

import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

@AllArgsConstructor
public abstract class BaseState {
    protected final DepositStorageService depositStorageService;

    public abstract Event execute(String reference);

    protected final DepositDto getDepositRequest(String reference) {
        final DepositDto depositRequest = depositStorageService.getRequest(reference);
        Assert.notNull(depositRequest, "deposit request must exist. Ref: " + reference);
        return depositRequest;
    }

    public boolean isFinal() {
        return getStateType().finalState;
    }

    public String toString() {
        return getStateType().toString();
    }

    public abstract State getStateType();
}
