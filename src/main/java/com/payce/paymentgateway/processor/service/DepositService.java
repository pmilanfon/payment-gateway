package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.acquier.AcquirerService;
import com.payce.paymentgateway.common.entity.MapStruct;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.rest.CardDetailsSubmit;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import com.payce.paymentgateway.processor.rest.DepositInitiateResponse;
import com.payce.paymentgateway.processor.rest.SubmitDepositRequest;
import com.payce.paymentgateway.processor.statemachine.StateMachine;
import com.payce.paymentgateway.processor.statemachine.event.TriggerStateMachineEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final StateMachine stateMachine;
    private final AcquirerService acquirerService;
    private final DepositStorageService depositStorageService;
    private final MapStruct mapStruct;


    public void submitCardDetails(CardDetailsSubmit cardDetailsSubmit) {
		stateMachine.onEvent(new TriggerStateMachineEvent(
				cardDetailsSubmit.getReference(),
				"New deposit submitted."));
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
				.replacePath("/api/payments/deposit/cashier/" + reference)
				.build()
				.toUriString();

		return cashierUrl;
	}
}
