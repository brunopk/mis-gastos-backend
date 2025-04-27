# Mis gastos backend

Home Assistant add-on which provides a REST API for [mis-gastos-web](https://github.com/brunopk/mis-gastos-web).

## Running Mis gastos with Home Assistant

### Requirements

- MariaDB Home Assistant add-on
- [TODO]

### Steps to install and run the add-on

1. [Configure Google credentials](doc/google.md) 
2. [TODO]

## Development

### Requirements

- MariaDB 10.11 (see `doc/db.md`)
- Java 21 (it can be installed with [SdkMan!](https://sdkman.io/))
- Maven (it can be installed with [SdkMan](https://sdkman.io/))

### Steps to run

1. Create database (see `doc/db.md`).
2. Configure database
3. Start the server :
    ```bash
    mvn spring-boot:run
    ```

Optionally, to run some Spring tasks that use Google Tasks, for instance to create tasks on Google to notify about spending, it is necessary to configure credentials as described in [this](doc/google.md) documentation.

To use Checkstyle for linting: 

```bash
mvn checkstyle:check
```

## Configuration

Configuration file (Spring properties) is `src/main/resources/application.yaml`.

- To change log-level set `logging.level.<PACKAGE-NAME>: <LEVEL>`
- Database configuration is defined in `spring.datasource`


Install [Spring Boot Assistant](https://plugins.jetbrains.com/plugin/17747-spring-boot-assistant) plugin on Intellij IDEA for YAML autocompletion.

## Building

```bash
mvn package
```

## Links

- [SdkMan!](https://sdkman.io/)
- [Tutorial: Making your first add-on](https://developers.home-assistant.io/docs/add-ons/tutorial)
- [Google credentials configuration](/doc/google.md)
