package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.common.entity.DepositEntity;
import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.processor.rest.DepositSubmitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final DepositRepository depositRepository;

    public void process(DepositSubmitRequest depositSubmitRequest) {
//        depositRepository.save(depositSubmitRequest.toEntity());
    }
}
