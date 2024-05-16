package com.payce.paymentgateway.security.response;

public record AuthResponse(String accessToken, String tokenType, Integer expiresIn) {
}