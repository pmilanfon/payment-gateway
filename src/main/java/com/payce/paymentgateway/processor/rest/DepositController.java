package com.payce.paymentgateway.processor.rest;

import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.service.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payments/deposit")
@RequiredArgsConstructor
public class DepositController {
	private final DepositService depositService;

	@PostMapping("/submit")
	public ResponseEntity<String> submitTransaction(@RequestBody DepositSubmitRequest depositSubmitRequest) {
		depositService.process(depositSubmitRequest);
		return new ResponseEntity<>("Deposit submitted successfully", HttpStatus.OK);
	}

	@PostMapping("/initiate")
	public ResponseEntity<String> initiateTransaction(@RequestBody DepositInitiateRequest depositInitiateRequest) {
		log.info("Init deposit {} ", depositInitiateRequest);
		String redirectUrl = depositService.initiate(depositInitiateRequest);
		return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
	}

	@GetMapping("/{merchantTxRef}")
	public ResponseEntity<DepositDto> getTransaction(@PathVariable String merchantTxRef) {
		log.info("Getting tx by merchant reference {}", merchantTxRef);
		DepositDto depositDto = depositService.get(merchantTxRef);
		return new ResponseEntity<>(depositDto, HttpStatus.OK);
	}
}
