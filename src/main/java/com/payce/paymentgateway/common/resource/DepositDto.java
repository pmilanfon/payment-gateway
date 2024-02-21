package com.payce.paymentgateway.common.resource;

import com.payce.paymentgateway.processor.statemachine.state.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Jacksonized
@AllArgsConstructor
@Getter
public class DepositDto {
    private String cardNumber;
    private String cardHolderName;
    private int expirationMonth;
    private int expirationYear;
    private String cvv;
    private String currency;
    private double amount;
    private LocalDate depositDate;
    private String merchantId;
    private String reference;
    private String externalId;
    private State currentState;
    private Instant created;
    private Instant stateUpdate;
    private String message;
}
