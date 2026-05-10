# Loki

[Grafana Loki](https://grafana.com/docs/loki/latest/), or sometimes abbreviated as Loki, is a logging tool. The [Quick start](https://grafana.com/docs/loki/latest/get-started/quick-start/quick-start/) explains very well the common Loki-Alloy-Grafana architecture. Loki should be installed on another machine or LXC container to receive logs from Alloy. To install Loki with `apt-get` follow these steps :

1. Add GPG keys for official repos :

    ```bash
    mkdir -p /etc/apt/keyrings/
    wget -q -O - https://apt.grafana.com/gpg.key | gpg --dearmor > /etc/apt/keyrings/grafana.gpg
    echo "deb [signed-by=/etc/apt/keyrings/grafana.gpg] https://apt.grafana.com stable main" | tee /etc/apt/sources.list.d/grafana.list
    ```
2. Update `apt-get` local registries :

    ```bash
    sudo apt-get update
    ```

3. Install `loki` package :

    ```bash
    sudo apt-get install loki
    ```

**Important :**
- Loki must be installed before Alloy

**Additional information :**
- The instructions to install Loki described above were extracted from https://apt.grafana.com/. Refer to the [Install using APT or RPM package manager
  ](https://grafana.com/docs/loki/latest/setup/install/local/#install-using-apt-or-rpm-package-manager) section of the official documentation for more information.
- Default Loki configuration file is stored in `/etc/loki/config.yml`.
- To check Loki is working correctly open http://localhost:3100/ready (replace *localhost* with the corresponding hostname).
- To visualize logs remotely, install Grafana and configure the corresponding Grafana Loki datasource in Grafana.