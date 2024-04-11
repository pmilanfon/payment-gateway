package com.payce.paymentgateway.acquier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcquirerService {

	private final AcqProperties config;
	private final RestClient rest;

	public String get() {
		log.info("Getting tx");
		return rest.get()
				.uri("/get")
				.retrieve()
				.body(String.class);
	}
}
