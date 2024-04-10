package com.payce.paymentgateway.processor.rest;

public record SubmitDepositRequest(DepositInitiateRequest deposit, CardDetailsSubmit card) {
}
