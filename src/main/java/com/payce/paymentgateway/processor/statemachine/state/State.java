package com.payce.paymentgateway.processor.statemachine.state;


public enum State {
    INITIATE(false),
    VALIDATE(false),
    CREATE_ORDER(false),
    PENDING_PROVIDER(false),
    EXECUTE(false),
    PENDING_TRANSACTION_ACCEPTANCE(false),
    PENDING_TRANSACTION_RESPONSE_DELAYED(false),
    PENDING_DEPOSIT_COMPLETED(false),
    DEPOSIT_SUCCESSFUL(true),
    FAILED(true);

    public final boolean isFinal;

    State(boolean isFinal) {
        this.isFinal = isFinal;
    }

}
