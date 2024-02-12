package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.processor.rest.DepositSubmitRequest;
import com.payce.paymentgateway.processor.statemachine.StateMachine;
import com.payce.paymentgateway.processor.statemachine.event.TriggerStateMachineEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final StateMachine stateMachine;

    public void process(DepositSubmitRequest depositSubmitRequest) {
        stateMachine.onEvent(new TriggerStateMachineEvent(
                depositSubmitRequest.getReferenceId(),
                "New deposit submitted."));
    }
}
