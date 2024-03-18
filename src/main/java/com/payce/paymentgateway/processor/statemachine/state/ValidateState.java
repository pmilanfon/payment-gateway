package com.payce.paymentgateway.processor.statemachine.state;


import com.payce.paymentgateway.common.service.DepositStorageService;
import com.payce.paymentgateway.processor.statemachine.event.ErrorMessage;
import com.payce.paymentgateway.processor.statemachine.event.Event;
import com.payce.paymentgateway.processor.statemachine.event.FailedEvent;
import com.payce.paymentgateway.processor.statemachine.event.GenericEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidateState extends BaseState {

    public ValidateState(DepositStorageService depositStorageService) {
        super(depositStorageService);
    }

    @Override
    public Event execute(String reference) {
        try {
            log.info("Validate");
            return new GenericEvent(StateMachineEvent.VALIDATION_SUCCESS_EVENT, reference);
        } catch (Exception e) {
            log.error("From VALIDATE, Caught Exception={}", e.getMessage());
            return new FailedEvent(reference,
                    new ErrorMessage("errorkey1", "errorkey2"));
        }
    }

    @Override
    public State getStateType() {
        return State.VALIDATE;
    }
}
