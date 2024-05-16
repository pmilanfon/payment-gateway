package com.payce.paymentgateway.security.requests;

import com.payce.paymentgateway.security.Role;

public record RegisterRequest(String firstName, String lastName, String email, String password, Role role,
                              String merchant) {
}
