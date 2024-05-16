package com.payce.paymentgateway.security.requests;

public record AuthRequest(String email, String password) {
}
