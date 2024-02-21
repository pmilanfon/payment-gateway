package com.payce.paymentgateway.processor.statemachine.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum State {
    INITIATE(false),
    VALIDATE(false),
    CREATE_ORDER(false),
    PENDING_PROVIDER(false),
    EXECUTE(false),
    PENDING_TRANSACTION_ACCEPTANCE(false),
    PENDING_TRANSACTION_RESPONSE_DELAYED(false),
    PENDING_DEPOSIT_COMPLETED(false),
    HANDLE_FAILURE(false),
    DEPOSIT_SUCCESSFUL(true),
    FAILED(true),
    FORCE_FAILED(true);

    public final boolean finalState;
}
