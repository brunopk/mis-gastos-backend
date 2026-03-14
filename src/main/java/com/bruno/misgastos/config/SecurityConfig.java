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
                    .ignoringRequestMatchers("/oauth2/token"))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .authorizeHttpRequests(
            (authorizationManagerRequestMatcherRegistry) ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            Customizer
                .withDefaults()) // Session login (by default this will permit request to /login)
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(Customizer.withDefaults())) // TODO: (this requires jwtdecoder bean)
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

  // TODO: add this to a Gist:
  //  1. the get-token endpoint below
  //  2. defaultSecurityFilterChain configuration above
  //  3. explain that the most important part is oauth2ResourceServer
  //  4. config   security:
  //    oauth2:
  //      resourceserver:
  //        jwt:
  //          secret-key

  /* @GetMapping("/get-token")
  public ResponseEntity<Object> getToken() {
    Base64.Decoder base64Decoder = Base64.getDecoder();
    SecretKey secretKey = Keys.hmacShaKeyFor(base64Decoder.decode(JWT_SECRET_KEY));
    Date now = new Date();
    Date expirationTime = new Date(System.currentTimeMillis() + 60 * 1000);
    String token = Jwts.builder()
      .setIssuedAt(now)
      .setClaims(Map.of("myField", "EXPECTED_VALUE"))
      .setExpiration(expirationTime)
      .signWith(secretKey)
      .compact();
    return ResponseEntity.ok(Map.of("token", token));
  } */

  /*@Bean
  public JwtDecoder jwtDecoder() {
    Base64.Decoder base64Decoder = Base64.getDecoder();
    byte[] secretAsByArray = base64Decoder.decode(JWT_SECRET_KEY);
    SecretKey key = new SecretKeySpec(secretAsByArray, JWT_SECRET_KEY_ALGORITHM);

    NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).build();

    OAuth2TokenValidator<Jwt> customValidator =
        jwt -> {
          String value = jwt.getClaim("myField");

          if (!"EXPECTED_VALUE".equals(value)) {
            return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_token", "Invalid custom field", null));
          }

          return OAuth2TokenValidatorResult.success();
        };

    decoder.setJwtValidator(customValidator);

    return decoder;
  }*/

  // Used for client credentials Oauth2 flow

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

  // Used for client credentials Oauth2 flow

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

  // TODO: document in a gist how to generate JWT tokens manually (find an all commit with this)

}
