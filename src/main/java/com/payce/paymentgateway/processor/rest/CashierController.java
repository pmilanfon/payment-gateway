package com.payce.paymentgateway.processor.rest;

import com.payce.paymentgateway.processor.service.DepositService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/api/payments/form")
@AllArgsConstructor
public class CashierController {

    private final DepositService depositService;

    @GetMapping("/{txRef}")
    public String getCashierForm(@PathVariable String txRef) {
        return "forward:/form/index.html";
    }

    @PostMapping
    public ResponseEntity<String> submitCardDetails(@RequestBody CardDetailsSubmit cardDetailsSubmit) {
        depositService.submitCardDetails(cardDetailsSubmit);
        return new ResponseEntity<>("Deposit submitted successfully", HttpStatus.OK);
    }
}

