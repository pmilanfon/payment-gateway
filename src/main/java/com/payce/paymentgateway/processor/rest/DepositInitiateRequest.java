package com.payce.paymentgateway.processor.rest;

import lombok.Data;

@Data
public class DepositInitiateRequest {
	private String merchantId;
	private String product;
	private double amount;
	private String currencyCode;
	private String merchantTxRef;
	private String orderDescription;
	private String billingAddress;
	private String address;
	private String transactionType;
	private String city;
	private String state;
	private String postCode;
	private String countryCode;
	private String emailAddress;
	private String phoneNumber;
	private String ipAddress;
	private String locale;
	private String dateOfBirth;
	private String firstName;
	private String lastName;
}
