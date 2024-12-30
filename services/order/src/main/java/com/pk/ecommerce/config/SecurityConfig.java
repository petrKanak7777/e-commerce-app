package com.pk.ecommerce.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Bean
    @SneakyThrows
    SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf().disable()
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.GET, "/eureka/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtConverter())))
                .build();
    }

    public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {
            final var authorities = Stream.concat(
                    jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                    extractResourceRoles(jwt).stream()).collect(Collectors.toSet()
            );

            return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
        }

        private String getPrincipalClaimName(final Jwt jwt) {
            return jwt.getClaim(JwtClaimNames.SUB);
        }

        private Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) {
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            Map<String, Object> resource;
            Collection<String> resourceRoles;

            if (resourceAccess == null
                    || (resource = (Map<String, Object>) resourceAccess.get(clientId)) == null
                    || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
                return Set.of();
            }

            return resourceRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
        }
    }
}