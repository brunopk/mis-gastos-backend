# Mis Gastos Backend

## Requirements

- Alloy (used to collect logs).
- Loki (used to receive logs).
- Grafana (used to visualize metrics and logs).
- MariaDB 10.11 (see `doc/db.md`).
- Java 21 (it can be installed with [SdkMan!](https://sdkman.io/) or `apt`).
- Maven (it can be installed with [SdkMan](https://sdkman.io/)).

### Additional information

- Alloy must be installed in the same machine or container where Mis Gastos Backend is running. Refer to [`/doc/alloy.md`](/doc/alloy.md) for more information about how to install and configure Alloy.
- Loki should be installed on another machine or LXC container to receive logs from Alloy. For more information refer to [`/doc/loki.md`](/doc/loki.md).
- Grafana must be installed on another machine or LXC container to visualize logs and metrics. To create Grafana dashboards refer to [`/doc/grafana.md`](/doc/grafana.md).

## Installation

1. Generate the JAR file with Maven :
      ```bash
      mvn package   
      ```

### Additional information


## Configuration

For **production**, the configuration file (Spring properties) is [`application-prod.yaml`](/src/main/resources/application-prod.yaml). Additionally, some of these properties refer to **environment variables** that must be defined :

- `DB_JDBC_URL`: `jdbc:mariadb://<HOSTNAME>:3306/<DATABASE>?serverTimezone=UTC`
- `DB_USER`: MariaDB username
- `DB_PASS`: MariaDB password
- `GOOGLE_CLIENT_ID`: Google client ID for Oauth2 (refer to [`doc/google.md`](/doc/google.md) for more information)
- `GOOGLE_CLIENT_SECRET`: Google client secret for Oauth2 (refer to [`doc/google.md`](/doc/google.md) for more information)
- `MIS_GASTOS_ADMIN_JWT_CLIENT_ID`: refer to [`/doc/scripts.md`](/doc/scripts.md) for more information
- `MIS_GASTOS_ADMIN_JWT_CLIENT_SECRET`: refer to [`/doc/scripts.md`](/doc/scripts.md) for more information

> Refer to [`/doc/spring.md`](/doc/spring.md) for details on the Spring configuration used in Mis Gastos Backend, including OAuth2, logging, and session management.

## Development

### Requirements

- MariaDB 10.11 (see `doc/db.md`)
- Java 21 (it can be installed with [SdkMan!](https://sdkman.io/))
- Maven (it can be installed with [SdkMan](https://sdkman.io/))

### Steps to run Mis Gastos Backend


To start the server :
    
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

## Documentation

> Documentation about different topics related or used by Mis Gastos Backend can be found in the [`/doc`](/doc) folder.

## Links

- [SdkMan!](https://sdkman.io/)
- [OAuth 2.0 and the Google OAuth Client Library for Java](https://developers.google.com/api-client-library/java/google-oauth-java-client/oauth2)
- [Google Tasks Java Quickstart](https://developers.google.com/workspace/tasks/quickstart/java)
- [Google API Client Libraries for Java](https://developers.google.com/api-client-library/java)
