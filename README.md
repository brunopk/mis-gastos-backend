# Mis gastos backend

Node.js backend for Mis gastos project. Based on [Node JS Server](https://github.com/brunopk/nodejs-server). Provides a REST API for [mis-gastos-web](https://github.com/brunopk/mis-gastos-web).

## Configurations

In order to run application correctly some environment variables must be defined:

- For production: `.env.production`
- For development: `.env.development`

Also they can be defined as usual with `export ENV_VAR=value` before running.

> To change [logging level](https://github.com/winstonjs/winston?tab=readme-ov-file#logging-levels), set `LOG_LEVEL`

## Building

1. Give permission for build script:

    ```bash
    chmod u+x build.sh
    ```

2. Build :

   ```bash
   yarn build
   ```

## Development

### Requirements

- MariaDB 10.11 (see `doc/db.md`).
- Node.js (recommended to install with NVM).

### Development with HMR (hot module reloading)

1. Create the database (see `doc/db.md`).
2. Configure database in `.env.development`
3. Start the server :

    ```bash
    yarn dev
    ```

## Links

- [Express](https://expressjs.com).
- [Node JS Server](https://github.com/brunopk/nodejs-server).
- [Developing an add-on](https://developers.home-assistant.io/docs/add-ons).
