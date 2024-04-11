package com.payce.paymentgateway.processor.statemachine.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "scheduler")
public class RetryOrFailProperties {
    protected boolean retryOrFailEnabled;

    private Map<String, Long> stateRetryTimeLimit = new HashMap<>();
    private Map<String, Long> stateFailTimeLimit = new HashMap<>();
    private Map<String, Long> stateLogAlertTimeLimit = new HashMap<>();
}
