package com.payce.paymentgateway.processor.statemachine.state;


import com.payce.paymentgateway.common.repo.DepositRepository;
import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.statemachine.event.ErrorMessage;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.FailedEvent;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateOrderState extends BaseState {

    public CreateOrderState(DepositRepository depositRepository) {
        super(State.CREATE_ORDER, null);
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
}
