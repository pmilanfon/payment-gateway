package com.payce.paymentgateway.processor.notification;

import com.payce.paymentgateway.common.entity.MapStruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

    private final MapStruct mapStruct;
    private final NotificationRepository notificationRepository;

    public void receive(AcquirerNotification notification) {
        final NotificationEntity notificationEntity = mapStruct.toEntity(notification);
        notificationRepository.save(notificationEntity);
    }
}
