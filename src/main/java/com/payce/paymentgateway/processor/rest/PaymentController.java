package com.payce.paymentgateway.processor.rest;

import com.payce.paymentgateway.processor.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody DepositSubmitRequest depositSubmitRequest) {
        paymentService.process(depositSubmitRequest);
        return new ResponseEntity<>("Payment processed successfully", HttpStatus.OK);
    }
}
