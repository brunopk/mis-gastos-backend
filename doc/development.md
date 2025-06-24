# Development

## Authentication

If you want to test authentication follow these steps:

1. Swap `@Profile` annotations between `defaultSecurityFilterChain` and `prodSecurityFilterChain` in `src/main/java/com/bruno/misgastos/config/SecurityConfig.java` in order to use `prodSecurityFilterChain`
2. Add CORS configuration: 
    ```java
      .cors(cors -> cors
        .configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(List.of("http://localhost:5173"));
          config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          config.setAllowedHeaders(List.of("*"));
          config.setAllowCredentials(true);
          return config;
        })
      )
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
   ```
   
   where `http://localhost:5173` is the local address on which mis-gastos-web is exposed 


To invoke endpoints that requires authentication through Home Assistant public address, extract `JSESSIONID` cookie with Chrome (inspect the site, go to "Application" tab and then go to "Cookies" on the left panel) and set it on Postman (click "Cookies" link below "Send" button).

To test with different session expiration times set `server.servlet.session.timeout` property like this :

```yaml
server:
  servlet:
    session:
      timeout: 5m
```