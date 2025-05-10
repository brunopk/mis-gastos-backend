# Database configuration

## Using MariaDB in Home Assistant

**First of all, database tables must be created with `sql/create_db`**. It's recommended to create a separate user who has access only to this database

You can use [Home Assistant Community Add-on: phpMyAdmin](https://github.com/hassio-addons/addon-phpmyadmin) to manage MariaDB databases in Home Assistant.

## Development

An easy way to install and run any Maria DB version is through Docker for local development:

```bash
docker run --name mariadb-container -e MARIADB_ROOT_PASSWORD=root -p 3306:3306 mariadb:10.11
```

Then :

1. Create **misgastos** using `sql/create_db.sql`
2. Insert test data using `sql/test_data.sql` (optional).
