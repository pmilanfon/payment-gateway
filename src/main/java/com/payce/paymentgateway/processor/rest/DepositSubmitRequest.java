package com.payce.paymentgateway.processor.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payce.paymentgateway.common.entity.DepositEntity;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.Currency;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositSubmitRequest {
    private final String cardNumber;
    private final String cardHolderName;
    private final int expirationMonth;
    private final int expirationYear;
    private final String cvv;
    private final BigDecimal amount;
    private final Currency currency;
    private final String merchantId;
}
