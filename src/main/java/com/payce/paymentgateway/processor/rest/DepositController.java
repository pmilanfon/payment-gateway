package com.payce.paymentgateway.processor.rest;

import com.payce.paymentgateway.common.resource.DepositDto;
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

    @PostMapping("/initiate")
    public ResponseEntity<String> initiate(@RequestBody DepositInitiateRequest initiateRequest) {
        depositService.initiate(initiateRequest);
        return new ResponseEntity<>("Deposit submitted successfully", HttpStatus.OK);
    }

    @PostMapping("/cardDetails")
    public ResponseEntity<String> submitCardDetails(@RequestBody CardDetailsSubmit cardDetailsSubmit) {
        depositService.submitCardDetails(cardDetailsSubmit);
        return new ResponseEntity<>("Deposit submitted successfully", HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitDeposit(@RequestBody SubmitDepositRequest submitDepositRequest) {
        depositService.submitDeposit(submitDepositRequest);
        return new ResponseEntity<>("Deposit submitted successfully", HttpStatus.OK);
    }

    @GetMapping("/{merchantTxRef}")
    public ResponseEntity<DepositDto> getTransaction(@PathVariable String merchantTxRef) {
        log.info("Getting tx by merchant reference {}", merchantTxRef);
        DepositDto depositDto = depositService.get(merchantTxRef);
        return new ResponseEntity<>(depositDto, HttpStatus.OK);
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<String> processPayment(@PathVariable String id) {
        log.info("Getting tx {}", id);
        depositService.(id);
        return new ResponseEntity<>("Deposit submitted successfully", HttpStatus.OK);
    }*/
}
