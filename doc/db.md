# Database

An easy way to install and run any Maria DB version is through Docker:

```bash
docker run --name mariadb-container -e MARIADB_ROOT_PASSWORD=root -p 3306:3306 mariadb:10.11
```

Then :

1. Create **misgastos** using `sql/create_db.sql`
2. Insert test data using `sql/test_data.sql` (optional).
