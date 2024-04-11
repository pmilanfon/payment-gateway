package com.payce.paymentgateway.processor.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Deposit")
public class DepositNotFoundException extends RuntimeException {
}
