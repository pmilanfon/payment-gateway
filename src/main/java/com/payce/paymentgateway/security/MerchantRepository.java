package com.payce.paymentgateway.security;

import com.payce.paymentgateway.security.entity.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

	Optional<MerchantEntity> findByName(String name);

	Optional<MerchantEntity> findByClientId(String clientId);

	boolean existsByName(String name);
}
