package com.payce.paymentgateway.acquier;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("acquirer")
@Configuration
@Getter
@Setter
public class AcqProperties {
	private String baseUrl;
}
