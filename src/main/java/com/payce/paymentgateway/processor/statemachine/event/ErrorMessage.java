package com.payce.paymentgateway.processor.statemachine.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
//todo exception instead of this?
public class ErrorMessage {
    private final String internalMessageKey;
    private final String customerMessageKey;
}
