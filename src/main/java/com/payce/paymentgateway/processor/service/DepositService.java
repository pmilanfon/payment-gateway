package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.acquier.AcquirerService;
import com.payce.paymentgateway.backoffice.BORequest;
import com.payce.paymentgateway.common.entity.DepositEntity;
import com.payce.paymentgateway.common.entity.MapStruct;
import com.payce.paymentgateway.common.entity.QDepositEntity;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.rest.CardDetailsSubmit;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import com.payce.paymentgateway.processor.rest.DepositInitiateResponse;
import com.payce.paymentgateway.processor.rest.SubmitDepositRequest;
import com.payce.paymentgateway.processor.statemachine.StateMachine;
import com.payce.paymentgateway.processor.statemachine.event.TriggerStateMachineEvent;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

	private final StateMachine stateMachine;
	private final AcquirerService acquirerService;
	private final DepositStorageService depositStorageService;
	private final MapStruct mapStruct;

	private void addIfNotNull(List<BooleanExpression> expressions, Object o, Supplier<BooleanExpression> r) {
		if (o != null) {
			expressions.add(r.get());
		}
	}

	public Page<DepositDto> getRequest(BORequest reference, Pageable page) {
		List<BooleanExpression> expressions = new ArrayList<>();
		addIfNotNull(expressions, reference.getMerchantId(),
				() -> QDepositEntity.depositEntity.merchantId.eq(reference.getMerchantId()));
		addIfNotNull(expressions, reference.getMerchantTxRef(),
				() -> QDepositEntity.depositEntity.merchantTxRef.eq(reference.getMerchantTxRef()));
		addIfNotNull(expressions, reference.getCardHolderName(),
				() -> QDepositEntity.depositEntity.cardHolderName.eq(reference.getCardHolderName()));
		addIfNotNull(expressions, reference.getReference(),
				() -> QDepositEntity.depositEntity.reference.eq(reference.getReference()));
		addIfNotNull(expressions, reference.getState(),
				() -> QDepositEntity.depositEntity.currentState.eq(reference.getState()));
		addIfNotNull(expressions, reference.getFromDate(),
				() -> QDepositEntity.depositEntity.depositDate.after(reference.getFromDate()));
		addIfNotNull(expressions, reference.getToDate(),
				() -> QDepositEntity.depositEntity.depositDate.before(reference.getToDate()));

		BooleanExpression booleanExpression = Expressions.asBoolean(true)
				.isTrue();
		for (BooleanExpression expression : expressions) {
			booleanExpression = booleanExpression.and(expression);
		}

		Page<DepositEntity> deposits = depositStorageService.find(booleanExpression, page);

		return deposits.map(mapStruct::toDto);
	}

	public void submitCardDetails(CardDetailsSubmit cardDetailsSubmit) {
		log.info("Updating deposit with card details");
		DepositEntity deposit = depositStorageService.getEntity(cardDetailsSubmit.getReference());
		mapStruct.toEntity(deposit, cardDetailsSubmit);
		depositStorageService.save(deposit);
		stateMachine.onEvent(new TriggerStateMachineEvent(
				cardDetailsSubmit.getReference(),
				"Card details submitted."));
	}

	public void submitDeposit(SubmitDepositRequest submitDepositRequest) {
		initiate(submitDepositRequest.deposit());
		submitCardDetails(submitDepositRequest.card());
	}

	public DepositDto get(String merchantTxRef) {
		return depositStorageService.findByMerchantTxRef(merchantTxRef);
	}

	public DepositInitiateResponse initiate(DepositInitiateRequest depositInitiateRequest) {
		final DepositDto initiatedDeposit = depositStorageService.initiate(depositInitiateRequest);
		final String reference = initiatedDeposit.getReference();
		final String url = constructCashierUrl(reference);

		return new DepositInitiateResponse(url, reference);
	}

	private static String constructCashierUrl(final String reference) {
		//todo temp solution, hateoas may be better suitable
		String cashierUrl = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.replacePath("/api/payments/form/" + reference)
				.build()
				.toUriString();

		return cashierUrl;
	}
}
