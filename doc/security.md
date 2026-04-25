# Security

Mis Gastos Backend supports two OAuth 2.0 flows:

- Authorization Code flow (using Google as the identity provider)
- Client Credentials flow 


## Authorization Code flow

Used to access Google APIs, such as Google Tasks and Gmail, on behalf of the user. Refer to [mis-gastos-web](https://github.com/brunopk/mis-gastos-web) to see how this flow is initiated.

## Client Credentials flow

As defined by the OAuth 2.0 standard, it is used for backend-to-backend authentication. For Mis Gastos Backend is mainly used in [scripts](/scripts). To authorize using the Client Credentials flow:

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
