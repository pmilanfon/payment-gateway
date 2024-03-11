package com.payce.paymentgateway.processor.rest;

import com.payce.paymentgateway.processor.service.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    /*@GetMapping("/{id}")
    public ResponseEntity<String> processPayment(@PathVariable String id) {
        log.info("Getting tx {}", id);
        depositService.(id);
        return new ResponseEntity<>("Deposit submitted successfully", HttpStatus.OK);
    }*/
}
