package com.payce.paymentgateway.backoffice;

import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.service.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/payments/back-office")
@RequiredArgsConstructor
public class BackOfficeController {
	private final DepositService service;

	@GetMapping
	public ResponseEntity<Page<DepositDto>> getTransaction(BORequest request,
	                                                       @PageableDefault Pageable page) {
		log.info("Getting transactions for BO {}", request);
		return ResponseEntity.ok(service.getRequest(request, page));
	}
}
