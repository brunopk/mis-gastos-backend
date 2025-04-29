# Using `build.sh`

It can be invoked like this:

```bash
./build.sh DB_HOSTNAME DB_NAME DB_USER DB_PASS
```

where: 

- `DB_HOSTNAME` is the database hostname (it can be found under **Hostname** in the MariaDB add-on screen). **Port is not required**
- `DB_NAME` is the database name
- `DB_USER` is the database username 

This will generate all required files into `build/` folder. As described in [Tutorial: Making your first add-on](https://developers.home-assistant.io/docs/add-ons/tutorial) all add-ons *must contain* at least the following files:

- `Dockerfile`
- `config.yaml`
- `run.sh`

In addition to these files this add-on must contain the following files:
   
- `application-prod.yaml`
- `icon.png`
- `README.md`
- `mis-gastos-backend.jar`

## Links 

- [Tutorial: Making your first add-on](https://developers.home-assistant.io/docs/add-ons/tutorial)
