package com.payce.paymentgateway.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity(name = "DEPOSIT")
@Data
public class DepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "expiration_month")
    private int expirationMonth;

    @Column(name = "expiration_year")
    private int expirationYear;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private double amount;

    @Column(name = "deposit_date")
    private LocalDate depositDate;

    @Column(name = "merchant_id")
    private String merchantId;
}
