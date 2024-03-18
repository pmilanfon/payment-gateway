package com.payce.paymentgateway.processor.statemachine.state;


import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.event.ErrorMessage;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.FailedEvent;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateOrderState extends BaseState {

    public CreateOrderState(DepositStorageService depositStorageService) {
        super(depositStorageService);
    }

    @Override
    public Event execute(String reference) {

        DepositDto depositRequest = getDepositRequest(reference);

        try {
            log.info("aaa");
            return new GenericEvent(StateMachineEvent.ORDER_CREATED_EVENT, reference);
        } catch (RuntimeException e) {
            log.info("bbb");
            return new FailedEvent(reference, new ErrorMessage("errorkey1", "errorkey2"));
        }
    }

    @Override
    public State getStateType() {
        return State.CREATE_ORDER;
    }
}
