package com.payce.paymentgateway.processor.rest;

import com.payce.paymentgateway.processor.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/deposit")
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody DepositSubmitRequest depositSubmitRequest) {
        depositService.process(depositSubmitRequest);
        return new ResponseEntity<>("Deposit submitted successfully", HttpStatus.OK);
    }
}
