package com.payce.paymentgateway.processor.rest;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class DepositSubmitRequest {
    private final String cardNumber;
    private final String cardHolderName;
    private final int expirationMonth;
    private final int expirationYear;
    private final String cvv;
    private final String amount;
    private final String currency;
    private final String merchantId;
    private final String reference;
}
