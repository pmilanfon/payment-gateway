package com.payce.paymentgateway.common.resource;

import java.time.LocalDate;

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
}
