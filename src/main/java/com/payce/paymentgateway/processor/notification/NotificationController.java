package com.payce.paymentgateway.processor.notification;

import com.payce.paymentgateway.processor.rest.DepositInitiateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/acquirer")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/notification")
    public ResponseEntity<DepositInitiateResponse> initiate(@RequestBody AcquirerNotification acquirerNotification) {
        notificationService.receive(acquirerNotification);
        return ResponseEntity.ok().build();
    }
}
