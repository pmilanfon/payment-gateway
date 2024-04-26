package com.payce.paymentgateway.common.entity;

import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.notification.AcquirerNotification;
import com.payce.paymentgateway.processor.notification.NotificationEntity;
import com.payce.paymentgateway.processor.rest.CardDetailsSubmit;
import com.payce.paymentgateway.processor.rest.DepositInitiateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MapStruct {

	@Named("maskCardNumber")
	static String maskCardNumber(String cardNumber) {
		return cardNumber == null ? null :
				cardNumber.substring(0, 4) + cardNumber.substring(4, cardNumber.length() - 1)
						.replaceAll(".", "*");
	}

	DepositEntity toEntity(DepositDto sendPayment);

	@Mapping(source = "cardNumber", target = "cardNumber", qualifiedByName = "maskCardNumber")
	DepositDto toDto(DepositEntity payment);

	DepositEntity toEntity(DepositInitiateRequest depositInitiateRequest);

	NotificationEntity toEntity(AcquirerNotification notification);
	void toEntity(@MappingTarget DepositEntity payment, CardDetailsSubmit cardDetailsSubmit);
}
