# Mis gastos backend

Spring Boot backend for Mis gastos project. Provides a REST API for [mis-gastos-web](https://github.com/brunopk/mis-gastos-web).

## Development

### Requirements

- MariaDB 10.11 (see `doc/db.md`)
- Java 21 (it can be installed with [SdkMan](https://sdkman.io/))
- Maven (it can be installed with [SdkMan](https://sdkman.io/))

### Steps to run

1. Create database (see `doc/db.md`).
2. Configure database
3. Start the server :

    ```bash
    mvn spring-boot:run
    ```

Database configuration is defined in `spring.datasource` within `src/main/resources/application.yaml`.

## Configuration

To set log-level change `logging.level.org.springframework.web` on `src/main/resources/application.yaml`. Install [Spring Boot Assistant](https://plugins.jetbrains.com/plugin/17747-spring-boot-assistant) plugin on Intellij IDEA for YAML autocompletion.

## Building

```bash
mvn package
```

## Links

- [SdkMan](https://sdkman.io/)
- [Developing an add-on](https://developers.home-assistant.io/docs/add-ons).
