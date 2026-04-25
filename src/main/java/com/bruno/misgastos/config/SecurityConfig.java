package com.bruno.misgastos.config;

import java.time.Duration;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
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


  @Bean
  @Profile({"default", "local"})
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable).build();
  }

  /**
   * Among other things, {@code oauth2Login} configuration provides an implementation for the GET /login endpoint.
   * @param http {@code SecurityFilterChain} bean provided by Spring
   * @return generated {@code SecurityFilterChain} bean
   * @throws Exception .
   */
  @Bean
  @Profile({"prod"})
  public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
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
          .withDefaults())
      .oauth2ResourceServer(
        oauth2 ->
          oauth2.jwt(Customizer.withDefaults()))
      .build();
  }

  /**
   * {@code OAuth2AuthorizationServerConfigurer} provides implementation for /oauth2/token, oauth2/authorize and
   * oauth2/jwks endpoints. Particularly for Mis Gastos Backend, the <strong>POST /oauth2/token</strong> endpoint can be
   * used to obtain <strong>JWT signed tokens for client credentials flow</strong>.
   * @param http {@code SecurityFilterChain} bean provided by Spring
   * @return generated {@code SecurityFilterChain} bean
   * @throws Exception .
   */
  @Bean
  @Profile({"prod"})
  @Order(1)
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        new OAuth2AuthorizationServerConfigurer();

    http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
        .with(authorizationServerConfigurer, configurer -> {});

    return http.build();
  }

  /**
   * Provides an instance of {code RegisteredClientRepository} required for the bean created by the
   * {@code authorizationServerSecurityFilterChain} method above (refer to {@code
   * authorizationServerSecurityFilterChain} method description for more information).
   *
   * @return generated {@code RegisteredClientRepository} bean
   */
  @Bean
  @Profile({"prod"})
  public RegisteredClientRepository registeredClientRepository(Environment env) {
    String MIS_GASTOS_ADMIN_CLIENT_ID = env.getRequiredProperty("mis-gastos.security.admin.client-id");
    String MIS_GASTOS_ADMIN_CLIENT_SECRET = env.getRequiredProperty("mis-gastos.security.admin.client-secret");
    int MIS_GASTOS_CLIENT_TOKEN_TTL_IN_HOURS =
        Integer.parseInt(env.getRequiredProperty("mis-gastos.security.jwt.ttl-in-hours"));

    RegisteredClient backendClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(MIS_GASTOS_ADMIN_CLIENT_ID)
            .clientSecret(String.format("{noop}%s", MIS_GASTOS_ADMIN_CLIENT_SECRET))
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofHours(MIS_GASTOS_CLIENT_TOKEN_TTL_IN_HOURS))
                    .build())
            .build();
    return new InMemoryRegisteredClientRepository(backendClient);
  }

}
