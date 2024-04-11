package com.payce.paymentgateway.common.resource;

import java.time.LocalDate;
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
    private double amount;
    private LocalDateTime depositDate;
    private String merchantId;
    private String reference;
    private String externalId;
    private State currentState;
    private Instant created;
    private Instant stateUpdate;
    private String message;
}
