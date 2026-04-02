package com.bruno.misgastos.config;

import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
  private String JWT_SECRET_KEY;

  private final String JWT_SECRET_KEY_ALGORITHM = "HmacSHA256";

  // TODO: move the corresponding configuration to prod environment

  // TODO: investigate what define the "Mis gastos" in the consent page of google

  // TODO: document that http://localhost:8080/oauth2/authorization/google is the URL for authorization code flow with Google

  @Bean
  @Profile({"default", "local"})
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(
            csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/oauth2/token")) // TODO: remove this restriction (security will be handled by Home Assistant)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .authorizeHttpRequests(
            (authorizationManagerRequestMatcherRegistry) ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()) // TODO: remove this authorization requirement (security will be handled by Home Assistant)
        .oauth2Login(
            Customizer
                .withDefaults()) // Session login (by default this will permit request to /login) // TODO verify if Google tokens can be obtaining after removing this line (security will be handled by Home Assistant)
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(Customizer.withDefaults())) // TODO: remove this authorization filter (security will be handled by Home Assistant)
        .build();
  }

  @Bean
  @Profile({"prod"})
  public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .authorizeHttpRequests(
            (authorizationManagerRequestMatcherRegistry) ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
      .build();
  }

  // Provides implementation for /oauth2/token, oauth2/authorize and oauth2/jwks endpoints
  // that are used to obtain OAuth 2.0 access tokens to perform actions on Google APIs on behalf of users

  @Bean
  @Order(1)
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        new OAuth2AuthorizationServerConfigurer();

    http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
        .with(authorizationServerConfigurer, configurer -> {});

    return http.build();
  }

  // TODO: set token duration in config

  // TODO: client-id in config

  // TODO: client-secret in config

  // Used to manage Google clients (applications) data in order to perform actions on Google APIs on behalf of users

  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    RegisteredClient backendClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("backend_app_test")
            .clientSecret("{noop}xxx")
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .tokenSettings(
                TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1)).build())
            .build();
    return new InMemoryRegisteredClientRepository(backendClient);
  }

 // TODO : change also frontend to use the new auth flow (no PKCE)

  // TODO: remove get-token endpoint (remove also from Postman collection)

  // TODO: refact GoogleRestClientImpl getToken is not needed any more , tokens are handled by spring libraries , authCallback endpoint is not needed anymore

  // TODO: document how to enable properties for security logging: org.springframework.security: trace, springframework.web.client: trace

  // TODO: document that /oauth2/authorization/google is the URL to initiate the authorization code flow (default URL)

}
