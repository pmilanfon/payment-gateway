package com.payce.paymentgateway.backoffice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BORequest {
	private final String merchantTxRef;
	private final String reference;
	private final String cardHolderName;
	private final String state;
	private final LocalDateTime toDate;
	private final LocalDateTime fromDate;
	private String merchantId;
}
