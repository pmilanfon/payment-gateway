package com.payce.paymentgateway.security;

import com.payce.paymentgateway.security.requests.AuthRequest;
import com.payce.paymentgateway.security.requests.RegisterMerchantRequest;
import com.payce.paymentgateway.security.requests.RegisterRequest;
import com.payce.paymentgateway.security.requests.TokenRequest;
import com.payce.paymentgateway.security.response.AuthResponse;
import com.payce.paymentgateway.security.response.RegisterMerchantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.OK)
	public void register(
			@RequestBody RegisterRequest request) {
		authService.register(request);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> authenticate(
			@RequestBody AuthRequest request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	@PostMapping(value = "/oauth2/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<AuthResponse> token(
			@RequestParam("client_id") String clientId,
			@RequestParam("client_secret") String clientSecret,
			@RequestParam("grant_type") String grantType) {
		log.info("Generating token for merchant ID {}", clientId);
		return ResponseEntity.ok(authService.token(new TokenRequest(clientId, clientSecret, grantType)));
	}

	@PostMapping("/create-merchant")
	public ResponseEntity<RegisterMerchantResponse> createMerchant(
			Authentication authentication,
			@RequestBody RegisterMerchantRequest request) {
		return ResponseEntity.ok(new RegisterMerchantResponse(request.name(), authService.createMerchant(request)));
	}
}
