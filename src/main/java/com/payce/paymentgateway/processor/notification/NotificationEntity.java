package com.payce.paymentgateway.processor.notification;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity(name = "NOTIFICATION")
@Accessors(chain = true)
@Data
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "status")
    private String status;
}
