# MariaDB

To prepare the MariaDB database for Mis Gastos Backend, follow instructions below : 

1. Install MariaDB
    <br><br>
    Visit [Download MariaDB Server](https://mariadb.org/download/?t=repo-config), select your Linux distribution and the latest MariaDB version, then follow the installation instructions.
    <br><br>

2. Configure Network Access
    <br><br>
    Edit `/etc/mysql/mariadb.conf.d/50-server.cnf` and update the `bind-address` to allow connections from other systems on the same local network:

    ```cnf
    bind-address = YOUR_SERVER_IP
    ```

3. Create a dedicated database user
    <br><br>
    ```sql
    CREATE USER 'misgastos_admin'@'%' IDENTIFIED BY 'strong_password';
    GRANT ALL PRIVILEGES ON misgastos.* TO 'misgastos_admin'@'%';
    FLUSH PRIVILEGES;
    ```
    
    The privileges will take effect once the database is created.
    <br><br>

4. Copy [`create_db.sql`](/sql/v2/create_db.sql)
    <br><br>
5. Create the database schema
    <br><br>
    Run the initialization script as **root** (or another MariaDB administrative user):

    ```bash
    mariadb < create_db.sql
    ```

    This script creates (or recreates) the database and initializes its schema.

## Development

Maria DB for development (local environment) can be easily installed with Docker:

```bash
docker run --name mariadb-container -e MARIADB_ROOT_PASSWORD=root -p 3306:3306 mariadb:10.11
```
Re-create the database executing [`create_db.sql`](/sql/v2/create_db.sql) with a database manager such as DBeaver. Optionally, insert some test data executing [`test_data_2.sql`](/sql/v2/test_data_2.sql).

## Useful SQL statements

### Listing databases

```sql
SHOW DATABASES;
```

### Listing users

```sql
SELECT User, Host FROM mysql.user;
```

### Deleting user

```sql
DROP USER 'myuser'@'%';
```

where `%` is the location at where the user was created, it may be an IP or `%` as wildcard.


## Useful Linux commands for database management

To get system timezone:

```bash
timedatectl
```