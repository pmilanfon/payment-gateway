package com.payce.paymentgateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.payce.paymentgateway.security.Role.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityWebConfiguration implements WebSecurityCustomizer {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        r -> r.requestMatchers("/error", "/h2-console/**")
                                .permitAll()
                                .requestMatchers("/api/payments/deposit/initiate")
                                .hasAuthority(MERCHANT_SERVICE.name())
                                .requestMatchers("/api/payments/deposit/**")
                                .permitAll()
                                .requestMatchers("/api/payments/back-office/**")
                                .hasAnyAuthority(MERCHANT_ADMIN.name(), SUPER_ADMIN.name())
                                .requestMatchers("/register", "/create-merchant")
                                .hasAuthority(SUPER_ADMIN.name())
                                .anyRequest()
                                .authenticated()
                )
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Override
    public void customize(WebSecurity web) {
        web.ignoring()
                .requestMatchers("/authenticate", "/oauth2/token");
    }
}
