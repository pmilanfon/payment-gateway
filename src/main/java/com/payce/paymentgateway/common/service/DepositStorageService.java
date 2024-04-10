package com.payce.paymentgateway.common.service;

import com.payce.paymentgateway.common.entity.DepositEntity;
import com.payce.paymentgateway.common.exception.DuplicateReferenceException;
import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import io.micrometer.core.annotation.Timed;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@AllArgsConstructor
@Service
public class DepositStorageService {

    private final DepositRepository depositRepository;

    @Timed
    public DepositDto getRequest(String reference) {
        return ofNullable(depositRepository.findByReference(reference))
                .map(DepositEntity::toDto)
                .orElseThrow(() -> new EntityNotFoundException(reference));
    }

    public void saveLatestRetry(String reference) {
        depositRepository.updateLatestRetry(Instant.now(), reference);
    }

    @Timed
    public DepositDto saveNewDepositDto(DepositDto depositDto) {
        DepositEntity depositRequestEntity =
                DepositEntity.fromDto(depositDto);
        try {
            return depositRepository.save(depositRequestEntity).toDto();
        } catch (DataIntegrityViolationException e) {
            log.error(
                    "Deposit request with reference {} already created",
                    depositDto.getReference(),
                    e);
            throw new DuplicateReferenceException("Deposit request with reference "
                    + depositDto.getReference()
                    + " already created", e);
        }
    }

    public List<DepositDto> findByStateAndTimeLimits(String state, Instant from, Pageable pageSize) {
        return depositRepository.findByStateAndTimeLimits(state, from, pageSize).stream()
                .map(DepositEntity::toDto)
                .toList();
    }

    public void saveMessage(String reference, String message) {
        DepositEntity depositRequestEntity = depositRepository
                .findByReference(reference);

        depositRequestEntity.setMessage(message);

        depositRepository.save(depositRequestEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DepositDto saveExternalId(String requestReference, String externalId) {
        DepositEntity entity = depositRepository
                .findByReference(requestReference)
                .setUpdated(Instant.now())
                .setExternalId(externalId);

        return depositRepository.save(entity).toDto();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void initiate(DepositInitiateRequest initiateRequest) {
        DepositEntity depositEntity = DepositEntity
                .fromDto(initiateRequest);

        depositRepository.save(depositEntity);

    }
}
