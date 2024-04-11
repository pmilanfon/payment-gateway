package com.payce.paymentgateway.common.repo;

import com.payce.paymentgateway.common.entity.DepositEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends CrudRepository<DepositEntity, Long> {
    DepositEntity findByReference(String reference);

    @Modifying
    @Query("update DEPOSIT ear set ear.latestRetry = :latestRetry where ear.reference = :ref")
    void updateLatestRetry(@Param("latestRetry") Instant latestRetry, @Param("ref") String ref);

    @Query(value = "SELECT dep FROM DEPOSIT dep WHERE dep.currentState= :currentState AND (dep.latestRetry < :latestRetry)")
    List<DepositEntity> findByStateAndTimeLimits(@Param("currentState") String currentState, @Param("latestRetry") Instant latestRetry,  Pageable pageable);

	Optional<DepositEntity> findByMerchantTxRef(String id);
}
