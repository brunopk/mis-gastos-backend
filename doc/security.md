# Security

Mis Gastos Backend supports two OAuth 2.0 flows:

- Authorization Code flow (using Google as the identity provider)
- Client Credentials flow 


## Authorization Code flow

Used to access Google APIs, such as Google Tasks and Gmail, on behalf of the user. For production use, Mis Gastos Backend uses **Google** as the OAuth 2.0 authorization provider for the **authorization code flow**. This flow can be initiated by doing a GET request to **http://localhost:8080/oauth2/authorization/google**, replacing *localhost* by the corresponding hostname where Mis Gastos Backend is running. This is the default URL defined by the *spring-boot-starter-oauth2-client* library.

## Client Credentials flow

As defined by the OAuth 2.0 standard, it is used for backend-to-backend authentication. To authorize using the Client Credentials flow:

1. Obtain a JWT access token by sending a POST request to http://localhost:8080/oauth2/token as defined by the Oauth2 Client Credentials standard :
    - Headers:
      - `Content-Type`: `application/x-www-form-urlencoded`
      - `Authorization`: result of encoding `client_id:client_secret` as a Base 64 string
    - Body parameters:
      - `grant_type`: `client_credentials`
2. Use the returned access token (`access_token` attribute) in the `Authorization` header when invoking and endpoint :
    ```HTTP
    Authorization: Bearer <access_token>
    ```
