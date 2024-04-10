package com.payce.paymentgateway.common.resource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
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
}
