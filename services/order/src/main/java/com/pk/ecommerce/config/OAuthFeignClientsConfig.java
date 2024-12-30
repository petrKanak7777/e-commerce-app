package com.pk.ecommerce.config;

import feign.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/*
 Based on this post and git example.
 link: https://dev.to/oigorrudel/spring-boot-feign-client-oauth2-4a7
  git: https://github.com/oigorrudel/spring-oauth2-example
 */
@Configuration
@EnableConfigurationProperties
public class OAuthFeignClientsConfig {

    @Value("${spring.cloud.openfeign.oauth2.clientRegistrationId}")
    private String registrationId;

    @Value("${spring.cloud.openfeign.oauth2.client-id}")
    private String clientId;

    @Value("${spring.cloud.openfeign.oauth2.client-secret}")
    private String clientSecret;

    @Value("${spring.cloud.openfeign.oauth2.token-uri}")
    private String tokenUri;

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        var clientRegistration = ClientRegistration.withRegistrationId(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri(tokenUri)
                .build();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    @Bean
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService(final ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository(final OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(oAuth2AuthorizedClientService);
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(final ClientRegistrationRepository clientRegistrationRepository,
                                                          final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository) {
        return new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientRepository);
    }

    @Bean
    Logger.Level loggerLevel() {
        return Logger.Level.NONE; //BASIC, FULL
    }
}