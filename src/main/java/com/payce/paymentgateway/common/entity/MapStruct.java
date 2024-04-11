package com.payce.paymentgateway.common.entity;

import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.notification.AcquirerNotification;
import com.payce.paymentgateway.processor.notification.NotificationEntity;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStruct {
	DepositEntity toEntity(DepositDto sendPayment);
	DepositDto toDto(DepositEntity payment);

	DepositEntity toEntity(DepositInitiateRequest depositInitiateRequest);

	NotificationEntity toEntity(AcquirerNotification notification);
//	void toEntity(@MappingTarget DepositEntity payment, DepositSubmitRequest depositInitiateRequest);
}
