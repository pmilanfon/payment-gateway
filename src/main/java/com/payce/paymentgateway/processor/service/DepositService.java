package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.acquier.AcquirerService;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import com.payce.paymentgateway.processor.rest.CardDetailsSubmit;
import com.payce.paymentgateway.processor.rest.SubmitDepositRequest;
import com.payce.paymentgateway.processor.statemachine.StateMachine;
import com.payce.paymentgateway.processor.statemachine.event.TriggerStateMachineEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final StateMachine stateMachine;
    private final AcquirerService acquirerService;
    private final DepositStorageService depositStorageService;

    public void submitCardDetails(CardDetailsSubmit cardDetailsSubmit) {
        stateMachine.onEvent(new TriggerStateMachineEvent(
                cardDetailsSubmit.getReference(),
                "New deposit submitted."));

    }

    public void initiate(DepositInitiateRequest initiateRequest) {
        depositStorageService.initiate(initiateRequest);
    }

    public void submitDeposit(SubmitDepositRequest submitDepositRequest) {
        initiate(submitDepositRequest.deposit());
        submitCardDetails(submitDepositRequest.card());
    }
}
