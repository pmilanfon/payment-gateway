package com.payce.paymentgateway.processor.notification;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {
    Optional<NotificationEntity> findByReference(String reference);
}
