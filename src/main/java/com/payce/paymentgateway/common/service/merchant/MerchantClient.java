package com.payce.paymentgateway.common.service.merchant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@AllArgsConstructor
public class MerchantClient {
  private final RestClient rest;

  public String postNotification() {
    log.info("Posting notification to merchant");
    return rest.post().uri("/merchant").retrieve().body(String.class);
  }
}
