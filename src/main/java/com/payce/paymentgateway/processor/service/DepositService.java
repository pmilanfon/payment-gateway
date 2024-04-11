package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.acquier.AcquirerService;
import com.payce.paymentgateway.common.entity.MapStruct;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.rest.CardDetailsSubmit;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import com.payce.paymentgateway.processor.rest.SubmitDepositRequest;
import com.payce.paymentgateway.processor.statemachine.StateMachine;
import com.payce.paymentgateway.processor.statemachine.event.TriggerStateMachineEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final StateMachine stateMachine;
    private final AcquirerService acquirerService;
    private final DepositStorageService depositStorageService;
    private final MapStruct mapStruct;


    public void submitCardDetails(CardDetailsSubmit cardDetailsSubmit) {
		depositStorageService.getRequest(cardDetailsSubmit.getReference());
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

	public void initiate(DepositInitiateRequest depositInitiateRequest) {
		depositStorageService.initiate(depositInitiateRequest);
	}
}
