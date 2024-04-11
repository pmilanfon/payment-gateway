package com.payce.paymentgateway.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "DEPOSIT")
@Accessors(chain = true)
@Data
@EntityListeners(AuditingEntityListener.class)
public class DepositEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "card_number")
	private String cardNumber;

	@Column(name = "card_holder_name")
	private String cardHolderName;

	@Column(name = "expiration_month")
	private int expirationMonth;

	@Column(name = "expiration_year")
	private int expirationYear;

	@Column(name = "cvv")
	private String cvv;

	@Column(name = "currency")
	private String currency;

	@Column(name = "amount")
	private double amount;

	@CreatedDate
	@Column(name = "deposit_date")
	private LocalDateTime depositDate;

	@Column(name = "merchant_id")
	private String merchantId;

	@Column(name = "reference")
	private String reference;

	@Column(name = "merchant_tx_ref")
	private String merchantTxRef;
}
