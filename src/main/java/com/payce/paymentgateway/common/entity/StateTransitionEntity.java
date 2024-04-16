package com.payce.paymentgateway.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity(name = "STATE_TRANSITION")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class StateTransitionEntity {
    @Id
    @UuidGenerator
    private String uuid;

    @ManyToOne(targetEntity = DepositEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_ref", nullable = false, updatable = false)
    private DepositEntity depositRequest;

    @Column(nullable = false, updatable = false)
    private String fromState;

    @Column(nullable = false, updatable = false)
    private String event;

    @Column(nullable = false, updatable = false)
    private String toState;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;
}
