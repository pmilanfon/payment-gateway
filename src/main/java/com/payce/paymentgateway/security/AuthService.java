package com.payce.paymentgateway.security;

import com.payce.paymentgateway.security.entity.MerchantEntity;
import com.payce.paymentgateway.security.entity.UserEntity;
import com.payce.paymentgateway.security.exception.MerchantAlreadyExist;
import com.payce.paymentgateway.security.exception.MerchantDoNotExist;
import com.payce.paymentgateway.security.requests.AuthRequest;
import com.payce.paymentgateway.security.requests.RegisterMerchantRequest;
import com.payce.paymentgateway.security.requests.RegisterRequest;
import com.payce.paymentgateway.security.requests.TokenRequest;
import com.payce.paymentgateway.security.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.payce.paymentgateway.security.JwtService.EXPIRE_TIME;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final MerchantRepository merchantRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public void register(RegisterRequest request) {
		MerchantEntity merchantEntity = merchantRepository.findByName(request.merchant())
				.orElseThrow(MerchantDoNotExist::new);
		UserEntity userEntity = UserEntity.builder()
				.firstName(request.firstName())
				.lastName(request.lastName())
				.email(request.email())
				.password(passwordEncoder.encode(request.password()))
				.role(request.role())
				.merchant(merchantEntity)
				.build();
		userRepository.save(userEntity);
	}

	public AuthResponse authenticate(AuthRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email(), request.password()));
		UserEntity userEntity = userRepository.findByEmail(request.email())
				.orElseThrow();
		return new AuthResponse(jwtService.createToken(userEntity), "Bearer", EXPIRE_TIME);
	}

	public AuthResponse token(TokenRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.clientId(), request.clientSecret()));
		MerchantEntity merchantEntity = merchantRepository.findByClientId(request.clientId())
				.orElseThrow();
		return new AuthResponse(jwtService.createToken(merchantEntity), "Bearer", EXPIRE_TIME);
	}

	public String createMerchant(RegisterMerchantRequest request) {
		if (merchantRepository.existsByName(request.name())) {
			throw new MerchantAlreadyExist();
		}
		return merchantRepository.save(MerchantEntity.builder()
						.clientId(UUID.randomUUID()
								.toString())
						.clientSecret(passwordEncoder.encode("12345"))
						.role(Role.MERCHANT_SERVICE)
						.name(request.name())
						.build())
				.getClientId();
	}
}
