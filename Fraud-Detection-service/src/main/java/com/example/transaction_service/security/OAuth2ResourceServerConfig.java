package com.example.transaction_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity

public class OAuth2ResourceServerConfig {

    private static final String[] AUTH_WHITELIST = {
            "/userss/me",
            "/cards", //
            "/banks",
            "/accounts",
            "/transactions",
            "/users",
            "/ws"


    };
    @Bean
    public JwtDecoder jwtDecoder() {
     //   return NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/spring-micro-main/protocol/openid-connect/certs").build();
        return NimbusJwtDecoder.withJwkSetUri("http://keycloak:8080/realms/spring-micro-main/protocol/openid-connect/certs").build();

    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(withDefaults())
                );
        return http.build();
    }



}
