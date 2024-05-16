package com.payce.paymentgateway.security.requests;

import org.springframework.web.bind.annotation.RequestParam;

public record TokenRequest(@RequestParam("client_id") String clientId, String clientSecret, String grantType) {
}
