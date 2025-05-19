package com.bruno.misgastos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringConfig {

  @Bean
  @Profile({"default", "local"})
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) -> {
        authorizationManagerRequestMatcherRegistry.requestMatchers("/oauth2/**").permitAll()
          .anyRequest().authenticated();
      }).oauth2ResourceServer(oauth2 -> oauth2
        .jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  @Profile({"prod"})
  public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) -> {
        authorizationManagerRequestMatcherRegistry.requestMatchers("/oauth2/**").permitAll()
          .anyRequest().authenticated();
      }).oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt ->
          jwt.decoder(JwtDecoders.fromIssuerLocation(issuerUri))
        ))
      .build();
  }
}
