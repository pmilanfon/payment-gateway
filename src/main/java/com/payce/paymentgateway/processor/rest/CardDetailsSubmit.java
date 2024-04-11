package com.payce.paymentgateway.processor.rest;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class CardDetailsSubmit {
    private final String cardNumber;
    private final String cardHolderName;
    private final int expirationMonth;
    private final int expirationYear;
    private final int cvv;
    private final String reference;
}
