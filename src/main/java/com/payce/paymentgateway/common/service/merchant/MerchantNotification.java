package com.payce.paymentgateway.common.service.merchant;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MerchantNotification(
        String merchantTxRef,
        BigDecimal amount,
        String currency,
        String status) {
}
