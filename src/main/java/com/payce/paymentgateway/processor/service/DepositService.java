package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.acquier.AcquirerService;
import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.processor.rest.DepositSubmitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
//    private final StateMachine stateMachine;
    private final AcquirerService acquirerService;

    public void process(DepositSubmitRequest depositSubmitRequest) {
//        stateMachine.onEvent(new TriggerStateMachineEvent(
//                depositSubmitRequest.getReferenceId(),
//                "New deposit submitted."));
    }

    public void get(String id) {
        log.info(acquirerService.get());
//        stateMachine.onEvent(new TriggerStateMachineEvent(
//                depositSubmitRequest.getReferenceId(),
//                "New deposit submitted."));
    }
}
