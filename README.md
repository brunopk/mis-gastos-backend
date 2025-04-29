# Mis gastos backend

Home Assistant add-on that provides a backend [mis-gastos-web](https://github.com/brunopk/mis-gastos-web).

## Requirements

- [Home Assistant Add-on: MariaDB](https://github.com/home-assistant/addons/tree/master/mariadb)
- [Home Assistant Add-on: NGINX Home Assistant SSL proxy](https://github.com/home-assistant/addons/tree/master/nginx_proxy)

Optionally but not required: 

- [Home Assistant Add-on: Samba share](https://github.com/home-assistant/addons/tree/master/samba)
- [Home Assistant Community Add-on: phpMyAdmin](https://github.com/hassio-addons/addon-phpmyadmin)

## Installation

1. [Configure Google credentials](https://github.com/brunopk/mis-gastos-backend/doc/google.md)
2. [Build the add-on with `build.sh`](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/building.md)
3. [Upload the add-on to Home Assistant](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/ha_add_ons_upload.md)
4. [Configure the database](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/db.md)
5. [Configure NGINX Home Assistant SSL proxy](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/nginx.md)

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
- [Google credentials configuration](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/google.md)
- [Uploading custom add-ons](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/ha_add_ons_upload.md)
- [Building Mis gastos](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/building.md)
- [Database configuration for Mis gastos](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/db.md)
- [NGINX configuration for Mis gastos](https://github.com/brunopk/mis-gastos-backend/blob/feature/initial/doc/nginx.md)
- [Home Assistant Add-on: MariaDB](https://github.com/home-assistant/addons/tree/master/mariadb)
- [Home Assistant Add-on: NGINX Home Assistant SSL proxy](https://github.com/home-assistant/addons/tree/master/nginx_proxy)
- [Home Assistant Add-on: Samba share](https://github.com/home-assistant/addons/tree/master/samba)
- [Home Assistant Community Add-on: phpMyAdmin](https://github.com/hassio-addons/addon-phpmyadmin)
- [Restricting Access with HTTP Basic Authentication](https://docs.nginx.com/nginx/admin-guide/security-controls/configuring-http-basic-authentication/)
