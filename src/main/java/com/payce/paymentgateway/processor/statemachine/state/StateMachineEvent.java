package com.payce.paymentgateway.processor.statemachine.state;

public enum StateMachineEvent {
    TRIGGER_STATE_MACHINE, //event only used for external trigger/start of statemachine
    INITIATION_SUCCESS_EVENT,
    VALIDATION_SUCCESS_EVENT,
    ORDER_CREATED_EVENT,
    FINANCIAL_TRANSACTION_POSTED_EVENT,
    PROVIDER_NOTIFIED_OK_EVENT,
    FAIL_EVENT,

    EXPIRED_EVENT,
    TRANSACTION_FINALIZED_EVENT,
    TRANSACTION_SUCCESSFUL_EVENT,
    TRANSACTION_RESPONSE_DELAY_EVENT,
    CANCELLED_BY_CUSTOMER,
    FORCED_FAIL //event only used when forcing transition to failed in retry job
}
