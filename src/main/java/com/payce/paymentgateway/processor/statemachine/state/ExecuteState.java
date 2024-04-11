package com.payce.paymentgateway.processor.statemachine.state;


import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.common.service.merchant.MerchantNotification;
import com.payce.paymentgateway.common.service.merchant.MerchantService;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExecuteState extends BaseState {

    private final MerchantService merchantService;

    public ExecuteState(DepositStorageService depositStorageService, final MerchantService merchantService) {
        super(depositStorageService);
        this.merchantService = merchantService;
    }

    @Override
    public Event execute(String reference) {
        final DepositDto deposit = depositStorageService.getRequest(reference);

        final MerchantNotification notification = MerchantNotification.builder()
                .currency(deposit.getCurrency())
                .merchantTxRef(deposit.getMerchantTxRef())
                .status("ok") //todo tmp, map from acquirer
                .amount(deposit.getAmount())
                .build();

        merchantService.postNotification(notification, deposit.getCallbackUrl());

        return new GenericEvent(StateMachineEvent.FINANCIAL_TRANSACTION_POSTED_EVENT, reference);
    }

    @Override
    public State getStateType() {
        return State.EXECUTE;
    }
}
