package com.payce.paymentgateway.common.resource;

import com.payce.paymentgateway.processor.statemachine.state.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private BigDecimal amount;
    private LocalDateTime depositDate;
    private String merchantId;
    private String reference;
    private String externalId;
    private State currentState;
    private LocalDateTime stateUpdate;
    private String message;
    private String merchantTxRef;
    private String callbackUrl;
    private String redirectUrl;
}
