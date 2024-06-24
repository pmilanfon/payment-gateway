package com.payce.paymentgateway.backoffice;

import com.payce.paymentgateway.common.resource.DepositDto;
import com.payce.paymentgateway.processor.service.DepositService;
import com.payce.paymentgateway.security.Role;
import com.payce.paymentgateway.security.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/payments/back-office")
@RequiredArgsConstructor
public class BackOfficeController {
    private final DepositService service;

    @GetMapping("/{txRef}")
    public String getBackofficeForm(@PathVariable String txRef) {
        return "forward:/backoffice/index.html";
    }

    @GetMapping("/transactions")
    public ResponseEntity<Page<DepositDto>> getTransaction(BORequest request,
                                                           @PageableDefault Pageable page) {
        log.info("Getting transactions for BO {}", request);
        UserEntity principal = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal.getRole().equals(Role.MERCHANT_ADMIN)) {
            request.setMerchantId(principal.getMerchant().getClientId());
        }
        log.info("{}", SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok(service.getRequest(request, page));
    }
}
