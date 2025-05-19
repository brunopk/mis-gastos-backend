package com.bruno.misgastos.config;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;
  
  @Bean
  @Profile({"default", "local"})
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  @Profile({"prod"})
  public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (authorizationManagerRequestMatcherRegistry) -> {
              authorizationManagerRequestMatcherRegistry
                  .requestMatchers("/oauth2/**")
                  .permitAll()
                  .anyRequest()
                  .authenticated();
            })
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    Base64.Decoder base64Decoder = Base64.getDecoder();
    byte[] secretAsByArray = base64Decoder.decode(jwtSecret);
    return new JwtAuthenticationFilter(secretAsByArray);
  }
}
