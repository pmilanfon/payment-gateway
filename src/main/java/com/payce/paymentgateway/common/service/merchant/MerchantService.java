package com.payce.paymentgateway.common.service.merchant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@AllArgsConstructor
public class MerchantService {
  private final RestClient rest;

  public ResponseEntity<Void> postNotification(final MerchantNotification notification, final String callbackUrl) {
    log.info("Posting notification to merchant");
    return rest.post().uri(callbackUrl).retrieve().toBodilessEntity();
  }
}
