package com.payce.paymentgateway.security;

import com.payce.paymentgateway.security.entity.MerchantEntity;
import com.payce.paymentgateway.security.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityBeansConfiguration {

    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final JwtService jwtService;

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }
//            return switch (authentication.getPrincipal()) {
//                case UserEntity u -> Optional.of(u.getUsername());
//                case MerchantEntity m -> Optional.of(m.getClientId());
//            };
            if (authentication.getPrincipal() instanceof UserEntity user) {
                return Optional.of(user.getUsername());
            }
            if (authentication.getPrincipal() instanceof MerchantEntity merchant) {
                return Optional.of(merchant.getClientId());
            }
            return Optional.of("UNKNOWN");
        };
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {
        userRepository.findByEmail("test")
                .ifPresentOrElse(user -> log.info("You can use SUPER ADMIN test/test user."),
                        () -> userRepository.save(
                                UserEntity.builder()
                                        .role(Role.SUPER_ADMIN)
                                        .email("test")
                                        .password("$2a$10$mvyzaCoG.oJzjpsDzIYjDuK75TsJUWJFmn04T.oUquRp20vqw9IE2")
                                        .firstName("First")
                                        .lastName("Last")
                                        .createdBy("AUTO")
                                        .build()));
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<MerchantEntity> merchant = merchantRepository.findByClientId(username);
            if (merchant.isPresent()) {
                return merchant.get();
            }
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtService, userDetailsService());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
