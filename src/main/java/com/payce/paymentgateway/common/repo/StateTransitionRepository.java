package com.payce.paymentgateway.common.repo;

import com.payce.paymentgateway.common.entity.StateTransitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateTransitionRepository extends JpaRepository<StateTransitionEntity, String> {}
