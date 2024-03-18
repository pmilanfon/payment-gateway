package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.acquier.AcquirerService;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import com.payce.paymentgateway.processor.rest.DepositSubmitRequest;
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

    public void process(DepositSubmitRequest depositSubmitRequest) {
        stateMachine.onEvent(new TriggerStateMachineEvent(
                depositSubmitRequest.getReference(),
                "New deposit submitted."));
    }

    public void initiate(DepositInitiateRequest initiateRequest) {
        depositStorageService.initiate()
    }
}
