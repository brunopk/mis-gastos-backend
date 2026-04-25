# Mis Gastos Backend

## Running Mis Gastos Backend

### Steps to run Mis Gastos Backend

1. Build the application with Maven to generate the JAR file.
2. Obtain Google credentials from the Google Console (refer to [`/doc/google.md`](/doc/google.md)).
3. Set the corresponding environment variables (refer to the [Configuration](#configuration) section below).

### Configuration

For **production**, the configuration file (Spring properties) is [`application-prod.yaml`](/src/main/resources/application-prod.yaml). Additionally, some of these properties refer to **environment variables** that must be defined :

- `DB_JDBC_URL`: `jdbc:mariadb://<HOSTNAME>:3306/<DATABASE>?serverTimezone=UTC`
- `DB_USER`: MariaDB username
- `DB_PASS`: MariaDB password
- `GOOGLE_CLIENT_ID`: Google client ID for Oauth2 (refer to [`doc/google.md`](/doc/google.md) for more information)
- `GOOGLE_CLIENT_SECRET`: Google client secret for Oauth2 (refer to [`doc/google.md`](/doc/google.md) for more information)
- `MIS_GASTOS_ADMIN_JWT_CLIENT_ID`: refer to [`/doc/scripts.md`](/doc/scripts.md) for more information
- `MIS_GASTOS_ADMIN_JWT_CLIENT_SECRET`: refer to [`/doc/scripts.md`](/doc/scripts.md) for more information

> Refer to [`/doc/spring.md`](/doc/spring.md) for details on the Spring configuration used in Mis Gastos Backend, including OAuth2, logging, and session management.

TEST (REMOVE THIS)

## Development

### Requirements

- MariaDB 10.11 (see `doc/db.md`)
- Java 21 (it can be installed with [SdkMan!](https://sdkman.io/))
- Maven (it can be installed with [SdkMan](https://sdkman.io/))

### Steps to run Mis Gastos Backend

1. Create database (see `doc/db.md`).
2. Configure database
3. Start the server :
    ```bash
    SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
    ```

Optionally, to run some Spring tasks that use Google Tasks, for instance to create tasks on Google to notify about spending, it is necessary to configure credentials as described in [this](doc/google.md) documentation.

To use Checkstyle for linting: 

```bash
mvn checkstyle:check
```

For more development tips, follow `doc/development.md`.

### Configuration

For **development**, the configuration file is [`application-local.yaml`](/src/main/resources/application-local.yaml).

> Refer to [`/doc/spring.md`](/doc/spring.md) for details on the Spring configuration used in Mis Gastos Backend, including OAuth2, logging, and session management.

### More documentation

- [`/doc/db.md`](/doc/db.md): how to create the DB and populate with test data
- [`/doc/development.md`](/doc/development.md): useful information for development
- [`/doc/google.md`](/doc/google.md): how to generate credentials in the Google Console
- [`/doc/google.md`](/doc/scripts.md): documentation about complementary Python scripts
- [`/doc/security.md`](/doc/security.md): OAuth2 flows supported by Mis Gastos Backend
- [`/doc/spring.md`](/doc/spring.md): details on the Spring configuration used in Mis Gastos Backend, including OAuth2, logging, and session management.


## Links

- [SdkMan!](https://sdkman.io/)
- [OAuth 2.0 and the Google OAuth Client Library for Java](https://developers.google.com/api-client-library/java/google-oauth-java-client/oauth2)
- [Google Tasks Java Quickstart](https://developers.google.com/workspace/tasks/quickstart/java)
- [Google API Client Libraries for Java](https://developers.google.com/api-client-library/java)
