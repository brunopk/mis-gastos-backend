# Spring

## Logging

- To debug security issues may be useful to enable *trace* or *debug* level for specific packages in Spring properties :
   ```yaml
   logging:
      level:
         org.springframework.security: trace
         springframework.web.client: trace
   ```
- The same idea mentioned above can be used to change logging level for any package : `logging.level.<PACKAGE-NAME>: <LEVEL>`

## OAuth2

- For production use, Mis Gastos Backend uses **Google** as the OAuth 2.0 authorization provider for the **authorization code flow**. In case of not having [mis-gastos-web](https://github.com/brunopk/mis-gastos-web) deployed, the authorization can be initiated by doing a GET request to **http://localhost:8080/oauth2/authorization/google**, replacing *localhost* by the corresponding hostname where Mis Gastos Backend is running. This is the default URL defined by the *spring-boot-starter-oauth2-client* library.
- The **http://localhost:8080/login** endpoint is provided by the *spring-boot-starter-security* dependency and renders a login page that presents the available authentication methods, including OAuth2 providers (e.g., Google) when configured.
- When using Spring Authorization Server with Nimbus JOSE+JWT (via *spring-security-oauth2-jose*, a transitive dependency of *spring-boot-starter-oauth2-authorization-server*), a default in-memory RSA key may be autoconfigured to sign JWTs (RS256) if no explicit `JWKSource` is defined; this is sufficient for the Mis Gastos Backend project, but **it should be replaced with a persistent key configuration for scalable or distributed environments**.

## Database

Database configuration is defined in `spring.datasource` in the corresponding properties file.

## Sessions

To set different session expiration times set `server.servlet.session.timeout` property.