# Uploading Home Assistant custom add-ons 

To upload a custom add-on you can use [Home Assistant Add-on: Samba share](https://github.com/home-assistant/addons/tree/master/samba). As described in [Tutorial: Making your first add-on](https://developers.home-assistant.io/docs/add-ons/tutorial) all add-ons *must contain* at least the following files:

- `Dockerfile`
- `config.yaml`
- `run.sh`

In addition to these files, Mis gastos add-on **must contain** the following files:

- `application-prod.yaml`
- `icon.png`
- `README.md`
- `mis-gastos-backend.jar`

## Links

- [Tutorial: Making your first add-on](https://developers.home-assistant.io/docs/add-ons/tutorial)