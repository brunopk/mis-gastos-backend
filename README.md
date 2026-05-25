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
      > By default, the JAR file is generated in the `target/` folder.
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
   
      > Replace each variable with the corresponding value, refer to the [Configuration](#configuration) section below.
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
   
      > Replace `mis-gastos-backend-0.0.1.jar` with the corresponding JAR.

## Configuration


Configuration is split across three property files: [`application.yaml`](src/main/resources/application.yaml), [`application-local.yaml`](src/main/resources/application-local.yaml), and [`application-prod.yaml`](src/main/resources/application-prod.yaml). The base configuration is defined in `application.yaml`, while environment-specific properties are overridden through Spring profiles:

- `local` → `application-local.yaml`
- `prod` → `application-prod.yaml`

Some properties reference **environment variables** that must be defined before starting the application :

| Variable                               | Description                                                                                                                                                                                                               |
|----------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `DB_JDBC_URL`                          | MariaDB JDBC URL. Must follow: `jdbc:mariadb://<HOSTNAME>:3306/<DATABASE>?serverTimezone=UTC`. For local development, this variable **must not** be defined because it is already configured in `application-local.yaml`. |
| `DB_USER`                              | MariaDB username. Should use a **new dedicated** MariaDB user. Refer to [`/doc/db.md`](/doc/db.md). For local development, this variable **must not** be defined.                                                         |
| `DB_PASS`                              | MariaDB password for `DB_USER`. For local development, this variable **must not** be defined.                                                                                                                             |
| `GOOGLE_CLIENT_ID`                     | Google OAuth client ID used to access Google APIs (for example Google Tasks and Gmail) on behalf of the user. See [Google credentials configuration](/doc/google.md#google-credentials-configuration).                    |
| `GOOGLE_CLIENT_SECRET`                 | Google OAuth client secret associated with `GOOGLE_CLIENT_ID`. See [Google credentials configuration](/doc/google.md#google-credentials-configuration).                                                                   |
| `GOOGLE_AUTHORIZED_ACCOUNT`            | Google email account authorized to perform actions on Mis Gastos Backend.                                                                                                                                                 |
| `GOOGLE_FIREBASE_SERVICE_ACCOUNT_JSON` | Path to the Firebase service account JSON file used for push notifications. Refer to [Push notifications with Firebase](/doc/google.md#push-notifications-with-firebase).                                                 |
| `GOOGLE_TASKS_TASK_LIST_ID`            | Google Tasks task list ID used for [scheduled tasks](/doc/tasks.md). It can be obtained from `http://localhost:8080/google/tasks/task-lists` (replace `localhost` with the corresponding hostname).                       |
| `MIS_GASTOS_ADMIN_JWT_CLIENT_ID`       | Client ID for the *admin* user used by [scripts](/scripts). Uses the [Client Credentials flow](/doc/security.md#client-credentials-flow).                                                                                 |
| `MIS_GASTOS_ADMIN_JWT_CLIENT_SECRET`   | Client secret associated with `MIS_GASTOS_ADMIN_JWT_CLIENT_ID`. Uses the [Client Credentials flow](/doc/security.md#client-credentials-flow).                                                                             |

</br>

> Refer to [`/doc/security.md`](/doc/security.md) for more information about the authentication flows used by Mis Gastos Backend.

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

> For local development, `DB_JDBC_URL`, `DB_USER`, and `DB_PASS`, **must not** be defined, since these values are already specified in `application-local.yaml`.

## Documentation

> Documentation about different topics related or used by Mis Gastos Backend can be found in the [`/doc`](/doc) folder.

## Links

- [SdkMan!](https://sdkman.io/)
- [OAuth 2.0 and the Google OAuth Client Library for Java](https://developers.google.com/api-client-library/java/google-oauth-java-client/oauth2)
- [Google Tasks Java Quickstart](https://developers.google.com/workspace/tasks/quickstart/java)
- [Google API Client Libraries for Java](https://developers.google.com/api-client-library/java)
- [What is Systemctl? An In-Depth Overview](https://www.liquidweb.com/blog/what-is-systemctl-an-in-depth-overview/)
