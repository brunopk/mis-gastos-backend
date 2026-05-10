# Security

Mis Gastos Backend supports two OAuth 2.0 flows:

- [Authorization Code flow](#authorization-code-flow): used to access Google APIs, such as Google Tasks and Gmail, on behalf of the user using Google as the OAuth2 provider.
- [Client Credentials flow](#client-credentials-flow): used to execute [scripts](/scripts).

## Authorization Code flow

This flow can be initiated by sending a GET request to `http://localhost:8080/oauth2/authorization/google`, replacing `localhost` with the hostname or domain where Mis Gastos Backend is running. This is the default endpoint provided by the `spring-boot-starter-oauth2-client` library.

> For more information about how to obtain the values for the `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` environment variables, refer to [`doc/google.md`](/doc/google.md).


## Client Credentials flow

To authorize using the Client Credentials flow:

1. Obtain a JWT access token by sending a POST request to http://localhost:8080/oauth2/token as defined by the Oauth2 Client Credentials standard :
    - Headers:
      - `Content-Type`: `application/x-www-form-urlencoded`
      - `Authorization`: result of encoding `client_id:client_secret` as a Base 64 string.
    - Body parameters:
      - `grant_type`: `client_credentials`
2. Use the returned access token (`access_token` attribute) in the `Authorization` header when invoking and endpoint :
    ```HTTP
    Authorization: Bearer <access_token>
    ```

> - The client ID and client secret are configured through the `MIS_GASTOS_ADMIN_JWT_CLIENT_ID` and `MIS_GASTOS_ADMIN_JWT_CLIENT_SECRET` environment variables, respectively.
> - As defined by the OAuth 2.0 standard, it is used for backend-to-backend authentication.