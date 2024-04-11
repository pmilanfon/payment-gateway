package com.payce.paymentgateway.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@Table(name = "STATE_TRANSITION")
@Accessors(chain = true)
@Entity
public class StateTransitionEntity {
    @Id
    @UuidGenerator
    private String uuid;

    @ManyToOne(targetEntity = DepositEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "reference", nullable = false, updatable = false)
    private DepositEntity depositRequest;

    @Column(nullable = false, updatable = false)
    private String fromState;

    @Column(nullable = false, updatable = false)
    private String event;

    @Column(nullable = false, updatable = false)
    private String toState;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;
}
