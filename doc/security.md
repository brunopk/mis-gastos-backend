# Security

Mis Gastos Backend is provided with two different OAuth 2.0 authorization flows:

- [Authorization Code flow](#authorization-code-flow): used to authorize users in [Mis Gastos Web](https://github.com/brunopk/mis-gastos-web).
- [Client Credentials flow](#client-credentials-flow): used for server-to-server authorization, for example to make requests in [scripts](/scripts).

## Authorization Code flow

This flow can be initiated by sending a GET request to `http://localhost:8080/oauth2/authorization/google` (replacing `localhost` with the hostname or domain where Mis Gastos Backend is running), this is the default endpoint provided by the `spring-boot-starter-oauth2-client` library. It makes possible to use Google APIs, such as Google Tasks and Gmail, on behalf of the user.

## Client Credentials flow

To authorize using the Client Credentials flow:

1. Obtain a JWT access token by sending a POST request to `http://localhost:8080/oauth2/token` as defined by the Oauth2 Client Credentials standard :
    - Headers:
      - Content-Type: `application/x-www-form-urlencoded`
      - Authorization: result of encoding `client_id:client_secret` as a Base 64 string.
    - Body parameters:
      - grant_type: `client_credentials`
2. Send obtained access token in the `Authorization` header when invoking an endpoint :
    ```HTTP
    Authorization: Bearer <access_token>
    ```
   
## Additional information

- The client ID and client secret are configured through the `MIS_GASTOS_ADMIN_JWT_CLIENT_ID` and `MIS_GASTOS_ADMIN_JWT_CLIENT_SECRET` environment variables, respectively.
- The Authorization Code flow is configured through the `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` environment variables. Refer to [`/doc/google.md`](/doc/google.md) to obtain their values.
- Enabling `trace` logging level for `org.springframework.security` and `springframework.web.client` packages may be particularly useful to troubleshoot security issues. Refer to the [Logging](/doc/spring.md#logging) section in [`(/doc/spring.md`](/doc/spring.md) for more information about how to configure logging in Spring.