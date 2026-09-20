package com.bruno.misgastos.config;

import com.bruno.misgastos.OAuth2LoginSuccessHandler;
import java.time.Duration;
import java.util.List;
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
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Among other things, {@code oauth2Login} configuration provides an implementation for the GET /login endpoint.
 * <br>
 * <br>
 * {@code IF_REQUIRED} is used because OAuth2 login relies on the HTTP session to persist the authenticated
 * {@code SecurityContext}. It allows Spring Security to create the session required to store the authentication and
 * reuse that same session for subsequent requests. {@code NEVER} is not suitable because Spring Security is prevented
 * from creating a session. If n suitable session exists when the authenticated {@code SecurityContext} needs to be
 * persisted, another component may create a new HTTP session later in the request lifecycle. That session receives a
 * new {@code JSESSIONID} and does not necessarily contain the authenticated SecurityContext, causing subsequent
 * requests to be associated with a different, unauthenticated session. {@code STATELESS} is not suitable because it
 * explicitly prevents the HTTP session from being used to persist the authenticated {@code SecurityContext}.
 */
@Configuration
public class SecurityConfig {

  /**
   * Filter chain configuration
   *
   * @param http Provided by Spring
   * @param oAuth2LoginSuccessHandler Used to map email to principal name (Google)
   * @return generated {@code SecurityFilterChain} bean
   * @throws Exception .
   */
  @Bean
  @Profile({"default", "local"})
  public SecurityFilterChain defaultSecurityFilterChain(
      HttpSecurity http, OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(
            (authorizationManagerRequestMatcherRegistry) ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth2LoginCustomizer ->
                oauth2LoginCustomizer.successHandler(oAuth2LoginSuccessHandler))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .build();
  }

  /**
   * Filter chain configuration
   *
   * @param http Provided by Spring
   * @param oAuth2LoginSuccessHandler Used to map email to principal name (Google)
   * @return generated {@code SecurityFilterChain} bean
   * @throws Exception .
   */
  @Bean
  @Profile({"prod"})
  public SecurityFilterChain prodSecurityFilterChain(
      HttpSecurity http, OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) throws Exception {
    return http.csrf(
            csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/oauth2/token"))
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(
            (authorizationManagerRequestMatcherRegistry) ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(oauth2LoginCustomizer -> oauth2LoginCustomizer.successHandler(oAuth2LoginSuccessHandler))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  @Profile({"default", "local"})
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
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

  /**
   * Required for Spring to automatically obtain refresh tokens using the current OAuth2 provider (Google).
   * @param registrations .
   * @param clientService .
   * @return .
   */
  @Bean
  OAuth2AuthorizedClientManager authorizedClientManager(
      ClientRegistrationRepository registrations, OAuth2AuthorizedClientService clientService) {

    OAuth2AuthorizedClientProvider provider =
        OAuth2AuthorizedClientProviderBuilder.builder().refreshToken().build();

    AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
        new AuthorizedClientServiceOAuth2AuthorizedClientManager(registrations, clientService);

    manager.setAuthorizedClientProvider(provider);

    return manager;
  }
}
