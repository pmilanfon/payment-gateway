package com.payce.paymentgateway.processor.statemachine.configuration;

import java.util.HashMap;
import java.util.Map;

public class RetryOrFailProperties {
    protected boolean retryOrFailEnabled;

    private Map<String, Long> stateRetryTimeLimit = new HashMap<>();
    private Map<String, Long> stateFailTimeLimit = new HashMap<>();
    private Map<String, Long> stateLogAlertTimeLimit = new HashMap<>();
}
