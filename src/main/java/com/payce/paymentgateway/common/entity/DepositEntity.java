package com.payce.paymentgateway.common.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.statemachine.state.State;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
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
    private Long id;
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
    private Instant latestRetry;
    @Enumerated(EnumType.STRING)
    private State currentState;
    private String message;
    private String externalId;
    @UpdateTimestamp
    private Instant updated;
    @CreationTimestamp
    private Instant created;
    private Instant stateUpdate;
    private String internalMessageKey;
    private String customerMessageKey;

    //todo temporary, try mapstruct?
    public DepositDto toDto() {
        try {
            String s = OBJECT_MAPPER.writeValueAsString(this);
            return OBJECT_MAPPER.readValue(s, DepositDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //todo temporary, try mapstruct?
    public static DepositEntity fromDto(DepositDto depositDto) {
        try {
            String s = OBJECT_MAPPER.writeValueAsString(depositDto);
            return OBJECT_MAPPER.readValue(s, DepositEntity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
