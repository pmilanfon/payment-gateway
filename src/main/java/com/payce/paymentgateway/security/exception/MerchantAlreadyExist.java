package com.payce.paymentgateway.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Merchant already exist")
public class MerchantAlreadyExist extends RuntimeException {
}
