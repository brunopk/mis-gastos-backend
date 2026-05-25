# Mis Gastos Backend

## Requirements

- [Alloy](/doc/alloy.md)
- [Loki](/doc/loki.md)
- [Grafana](/doc/grafana.md)
- [MariaDB](/doc/db.md)
- Java 21 JRE
- Maven

<br>

> Maven can be installed with [SdkMan!](https://sdkman.io/) and the JRE with SdkMan! or the APT package manager.

## Installation

1. Generate the JAR file with Maven :
    ```bash
    mvn package   
    ```
    By default, the JAR file is generated in the `target/` folder.
2. Create the `/root/mis-gastos-backend.env` file for environment variables : 
    ```dotenv
    SPRING_PROFILES_ACTIVE=prod
    DB_JDBC_URL=jdbc:mariadb://<HOSTNAME>:3306/<DATABASE>?serverTimezone=UTC
    DB_USER=root
    DB_PASS=root
    GOOGLE_CLIENT_ID=123456-xxx.apps.googleusercontent.com
    GOOGLE_CLIENT_SECRET=xxx
    GOOGLE_AUTHORIZED_ACCOUNT=your_mail@gmail.com
    GOOGLE_FIREBASE_SERVICE_ACCOUNT_JSON=/path/to/json
    GOOGLE_TASKS_TASK_LIST_ID=xxx
    MIS_GASTOS_ADMIN_JWT_CLIENT_ID=admin
    MIS_GASTOS_ADMIN_JWT_CLIENT_SECRET=admin
    ```
    **Replace each variable with the corresponding value**, refer to the [Configuration](#configuration) section below.
3. Create the `/etc/systemd/system/mis-gastos-backend.service` unit file for the Linux service : 
    ```unit
    [Unit]
    Description=Mis Gastos Backend server
    
    [Service]
    Type=simple
    ExecStart=java -jar /root/mis-gastos-backend-0.0.1.jar
    User=root
    Restart=on-failure
    RestartSec=2
    EnvironmentFile=/root/mis-gastos-backend.env
    
    [Install]
    WantedBy=multi-user.target
    ```
    
    **Replace `ExecStart=java -jar /root/mis-gastos-backend-0.0.1.jar` with the corresponding JAR version.**

## Configuration


Configuration is split across three property files: [`application.yaml`](src/main/resources/application.yaml), [`application-local.yaml`](src/main/resources/application-local.yaml), and [`application-prod.yaml`](src/main/resources/application-prod.yaml).

The base configuration is defined in `application.yaml`, while environment-specific properties are overridden through Spring profiles:

- `local` → `application-local.yaml`
- `prod` → `application-prod.yaml`

Some properties reference **environment variables** that must be defined before starting the application :

- `DB_USER` and `DB_PASS` should be set with the credentials of a new dedicated MariaDB user. The value for `DB_JDBC_URL` must follow this pattern: `jdbc:mariadb://<HOSTNAME>:3306/<DATABASE>?serverTimezone=UTC`. **For local development, `DB_JDBC_URL`, `DB_USER`, and `DB_PASS`, **must not** be defined, since these values are already specified in `application-local.yaml`.** Refer to [`/doc/db.md`](/doc/db.md) for more information about database configuration.
- `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` are set with the corresponding values that can be obtained following instructions in the [Google credentials configuration](/doc/google.md#google-credentials-configuration) section from [`/doc/google.md`](/doc/google.md). Refer also to the [Authorization Code flow](/doc/security.md#authorization-code-flow) section in [`/doc/security.md`](/doc/security.md) for more information about Google.
- `GOOGLE_AUTHORIZED_ACCOUNT` is the Google email account authorized to perform actions on Mis Gastos Backend. 
- `GOOGLE_FIREBASE_SERVICE_ACCOUNT_JSON` is the path to the JSON file with service account credentials for Firebase. Refer to the [Push notifications with Firebase](/doc/google.md#push-notifications-with-firebase) section in [`/doc/google.md`](/doc/google.md) for more information about how to obtain this file.
- `GOOGLE_TASKS_TASK_LIST_ID` is used for [scheduled tasks](/doc/tasks.md). This ID can be obtained by navigating to http://localhost:8080/google/tasks/task-lists, replacing *localhost* with the corresponding hostname where Mis Gastos Backend is running.
- `MIS_GASTOS_ADMIN_JWT_CLIENT_ID` and `MIS_GASTOS_ADMIN_JWT_CLIENT_SECRET` are the credentials ([Client Credentials flow](/doc/security.md#client-credentials-flow)) for the *admin* user of Mis Gastos Backend. These credentials are used to execute [scripts](/scripts), currently only **one** user can be configured. Refer to the [Client Credentials flow](/doc/security.md#client-credentials-flow) section in [`/doc/security.md`](/doc/security.md) for more information about these environment variables.


## Development

### Requirements

- [MariaDB](/doc/db.md)
- Java 21 JRE
- Maven

<br>

> Maven can be installed with [SdkMan!](https://sdkman.io/) and the JRE with SdkMan! or the APT package manager.

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
- [What is Systemctl? An In-Depth Overview](https://www.liquidweb.com/blog/what-is-systemctl-an-in-depth-overview/)
