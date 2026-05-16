# Alloy

As described in its official documentation, Grafana Alloy is an open source telemetry **collector** for metrics, logs, traces, and continuous profiles. As explained in the [Quickstart to run Loki locally](https://grafana.com/docs/loki/latest/get-started/quick-start/quick-start/#quickstart-to-run-loki-locally), Alloy is part of a big architecture often built with Docker. **Alloy must be installed in the same machine or container where the main process is running**. To install and configure Alloy follow these steps :

1. Add GPG keys for official repos (if not done before) and update the APT registries as described in [`loki.md`](/doc/loki.md).
2. Add this configuration to `/etc/alloy/config.alloy`:

   ```
   loki.source.journal "read" {
     forward_to = [loki.process.parse_logs.receiver]
     matches    = "_SYSTEMD_UNIT=sc-rpi.service"
     labels     = {service = "sc-rpi"}
   }

   loki.process "parse_logs" {
     forward_to = [loki.write.endpoint.receiver]

     stage.logfmt {
       mapping = {
         level = "level",
         msg = "event",
       }
     }

     stage.labels {
       values = {
       level = "",
     }
   }

   loki.write "endpoint" {
     endpoint {
       url = "http://localhost:3100/loki/api/v1/push"
     }
   }
   ```
3. Enable the service (if it is not enabled yet):

   ```bash
   systemctl enable alloy
   ```
4. Start the service :

   ```bash
   systemctl start alloy
   ```

**Important :**
- Alloy must be installed after Loki

**Additional information :**
- To verify that Alloy is running :
    1. Add this configuration to `/etc/default/alloy` :
          ```
          # User-defined arguments to pass to the run command.
          CUSTOM_ARGS="--server.http.listen-addr=0.0.0.0:12345"
          ```
    2. Restart the service
    3. Open http://mis-gastos-backend.internal:12345 (replace *mis-gastos-backend.internal* with the corresponding hostname)
- The instructions to install Alloy described above were extracted from https://apt.grafana.com/, Refer to the [Install](https://grafana.com/docs/alloy/latest/set-up/install/linux/#install) section of the official documentation for more information.
