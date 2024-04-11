package com.payce.paymentgateway.common.service;

import com.payce.paymentgateway.common.entity.DepositEntity;
import com.payce.paymentgateway.common.entity.MapStruct;
import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import com.payce.paymentgateway.processor.statemachine.state.State;
import io.micrometer.core.annotation.Timed;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Slf4j
@AllArgsConstructor
@Service
public class DepositStorageService {

    private final DepositRepository depositRepository;
    private final MapStruct mapStruct;

    @Timed
    public DepositDto getRequest(String reference) {
        return ofNullable(depositRepository.findByReference(reference))
                .map(mapStruct::toDto)
                .orElseThrow(() -> new EntityNotFoundException(reference));
    }

    @Timed
    public DepositEntity getEntity(String reference) {
        return ofNullable(depositRepository.findByReference(reference))
                .orElseThrow(() -> new EntityNotFoundException(reference));
    }

    @Timed
    public DepositDto findByMerchantTxRef(String merchantTxRef) {
        return depositRepository.findByMerchantTxRef(merchantTxRef)
                .map(mapStruct::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found by merchant tx ref " + merchantTxRef));
    }

    public void saveLatestRetry(String reference) {
        depositRepository.updateLatestRetry(Instant.now(), reference);
    }

    public List<DepositDto> findByStateAndTimeLimits(String state, Instant from, Pageable pageSize) {
        return depositRepository.findByStateAndTimeLimits(state, from, pageSize).stream()
                .map(mapStruct::toDto)
                .toList();
    }

    public void initiate(final DepositInitiateRequest depositInitiateRequest) {
        DepositEntity depositEntity = mapStruct.toEntity(depositInitiateRequest)
                .setReference(UUID.randomUUID().toString())
                .setCurrentState(State.INITIATE.name());

        depositRepository.save(depositEntity);
        log.info("Saved new deposit {}", depositEntity);
    }
}
