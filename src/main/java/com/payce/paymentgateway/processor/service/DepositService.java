package com.payce.paymentgateway.processor.service;

import com.payce.paymentgateway.acquier.AcquirerService;
import com.payce.paymentgateway.common.entity.DepositEntity;
import com.payce.paymentgateway.common.entity.MapStruct;
import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import com.payce.paymentgateway.processor.rest.DepositSubmitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

	private final DepositRepository depositRepository;
	//    private final StateMachine stateMachine;
	private final AcquirerService acquirerService;
	private final MapStruct mapStruct;

	public void process(DepositSubmitRequest depositSubmitRequest) {
		DepositEntity depositEntity = depositRepository.findByReference(depositSubmitRequest.getReference());
		mapStruct.toEntity(depositEntity, depositSubmitRequest);
		depositRepository.save(depositEntity);
//        stateMachine.onEvent(new TriggerStateMachineEvent(
//                depositSubmitRequest.getReferenceId(),
//                "New deposit submitted."));
	}

	public DepositDto get(String merchantTxRef) {
		return depositRepository.findByMerchantTxRef(merchantTxRef)
				.map(mapStruct::toDto)
				.orElseThrow(DepositNotFoundException::new);
	}

	public String initiate(DepositInitiateRequest depositInitiateRequest) {
		DepositEntity depositEntity = mapStruct.toEntity(depositInitiateRequest);
		depositEntity.setReference(UUID.randomUUID().toString());
		depositRepository.save(depositEntity);
		log.info("Saved new deposit {}", depositEntity);
		return "http://localhost:9090";
	}
}
