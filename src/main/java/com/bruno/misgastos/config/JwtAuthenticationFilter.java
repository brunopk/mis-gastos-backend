package com.bruno.misgastos.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring filter based on <a
 * href="https://medium.com/@AlexanderObregon/how-spring-boot-implements-jwt-authentication-without-sessions-0026afbe66bf">How
 * Spring Boot Implements JWT Authentication Without Sessions<a/> In this implementation, {@code
 * UserDetailsService} is not used.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final byte[] secret;

  public JwtAuthenticationFilter(byte[] secret) {
    this.secret = secret;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain)
      throws IOException, ServletException {
    String token = getTokenFromRequest(request);

    if (token != null && validateToken(token)) {
      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(null, null, Collections.emptyList());
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    chain.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
  }

  private boolean validateToken(String token) {
    try {
      Key key = Keys.hmacShaKeyFor(secret);
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      LOGGER.debug("JWT error: ", e);
      return false;
    }
  }
}
