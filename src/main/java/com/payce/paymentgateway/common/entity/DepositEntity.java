package com.payce.paymentgateway.common.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.payce.paymentgateway.common.resource.DepositDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

//todo consider https://medium.com/miro-engineering/fluent-setter-breaking-the-convention-33ce3433126e
@Accessors(chain = true)
@Entity(name = "DEPOSIT")
@Data
public class DepositEntity {

    //todo delete
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

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

    @Column(name = "reference")
    private String reference;

    //todo temporary, try mapstruct?
    public DepositDto toDto() {
        try {
            String s = OBJECT_MAPPER.writeValueAsString(this);
            return OBJECT_MAPPER.readValue(s, DepositDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
