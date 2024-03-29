package com.payce.paymentgateway.common.repo;

import com.payce.paymentgateway.common.entity.DepositEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends CrudRepository<DepositEntity, Long> {
    DepositEntity findByReference(String reference);
}
