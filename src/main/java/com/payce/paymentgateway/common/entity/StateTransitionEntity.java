package com.payce.paymentgateway.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Data
@Table(name = "STATE_TRANSITION")
public class StateTransitionEntity {
    @Id
    @UuidGenerator
    private String uuid;

    @ManyToOne(targetEntity = DepositEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPOSIT_REF", nullable = false, updatable = false)
    private DepositEntity depositRequest;

    @Column(name = "FROM_STATE", nullable = false, updatable = false)
    private String fromState;

    @Column(name = "EVENT", nullable = false, updatable = false)
    private String event;

    @Column(name = "TO_STATE", nullable = false, updatable = false)
    private String toState;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    private Instant created;
}
