package com.payce.paymentgateway.acquier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcqRequest {
	private final String cardNumber;
	private final String cardCvv;
	private final String cardExpiry;
	private final String transactionId;
	private final double tranAmount;
	private final String tranCurrency;
	private final String tranType;
}
