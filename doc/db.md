# Database configuration

## Using MariaDB in Home Assistant

**First of all, database tables must be created with `sql/create_db`**. It's recommended to create a separate user who has access only to this database

## Development

To install and run Maria DB for development (local environment) with Docker:

```bash
docker run --name mariadb-container -e MARIADB_ROOT_PASSWORD=root -p 3306:3306 mariadb:10.11
```

Then :

1. Create **misgastos** using `sql/create_db.sql`
2. Insert test data using `sql/test_data.sql` (optional).
