package com.example.user_service.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration


public class KeycloakConfig {
    //c est le meme pour l obtenir du token admmin en postman
    private static final Logger logger = LoggerFactory.getLogger(KeycloakConfig.class);


    @Bean
    public Keycloak keycloak() {
        try {
            return KeycloakBuilder.builder()
                    .serverUrl("http://localhost:8080")
                    .realm("master")
                    .clientId("admin-cli")
                    .clientSecret("jwpk9VN2Z8Al4vutOJYuR1x4ADJiQmPm")
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        } catch (Exception e) {
            logger.error("Error creating Keycloak instance", e);
            throw new RuntimeException("Failed to create Keycloak client", e);
        }

    }
}