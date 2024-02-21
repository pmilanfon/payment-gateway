package com.payce.paymentgateway.common.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateReferenceException extends RuntimeException {
    public DuplicateReferenceException(String msg, DataIntegrityViolationException e) {
        super(msg, e);
    }
}
