package com.payce.paymentgateway.common.repo;

import com.payce.paymentgateway.common.entity.DepositEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepositRepository extends CrudRepository<DepositEntity, Long> {
	DepositEntity findByReference(String reference);

	Optional<DepositEntity> findByMerchantTxRef(String id);
}
