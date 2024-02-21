package com.payce.paymentgateway.acquier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AcqConfig {
	@Bean
	public RestClient restClient(RestClient.Builder builder, AcqProperties acqProperties) {
		return builder.baseUrl(acqProperties.getBaseUrl()).build();
	}

}
