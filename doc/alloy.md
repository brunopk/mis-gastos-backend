# Alloy

As described in its official documentation, Grafana Alloy is an open source telemetry **collector** for metrics, logs, traces, and continuous profiles. As explained in the [Quickstart to run Loki locally](https://grafana.com/docs/loki/latest/get-started/quick-start/quick-start/#quickstart-to-run-loki-locally), Alloy is part of a big architecture often built with Docker. **Alloy must be installed in the same machine or container where the main process is running**. To install and configure Alloy follow these steps :

1. Add GPG keys for official repos (if not done before) and update the APT registries as described in [`loki.md`](/doc/loki.md).
2. Add this configuration to `/etc/alloy/config.alloy`:

    ```hcl
    loki.source.journal "spring_journal" {
    // Filter to your specific systemd unit
        matches = "_SYSTEMD_UNIT=mis-gastos-backend.service"
    
        // Forward to the JSON processing stage
        forward_to = [loki.process.parse_json.receiver]
    
        labels = {
            service  = "mis-gastos-backend",
            env  = "prod",
        }
    }

    // 2. PROCESS & PARSE JSON PIPELINE
    loki.process "parse_json" {
    
        // Stage 1: Parse the raw JSON log line
        stage.json {
            expressions = {
                level      = "level",
                trace_id   = "trace_id",
                user_id    = "user_id",
                logger     = "logger_name",
                msg        = "message",
                stack_trace = "stack_trace",
            }
        }
    
        // Stage 2: Promote extracted fields to Loki labels
        // Keep labels LOW cardinality (avoid user_id here!)
        stage.labels {
            values = {
                level  = "level",
                logger = "logger",
            }
        }
    
        // Stage 3: Put high-cardinality fields in structured metadata
        // Queryable via LogQL but not stored as labels
        stage.structured_metadata {
            values = {
                trace_id = "trace_id",
                user_id  = "user_id",
            }
        }
    
        // Stage 4: Use the app timestamp, not ingestion time
        stage.timestamp {
            source = "@timestamp"
            format = "RFC3339Nano"
        }
    
        // Stage 5: Rewrite log line to just the message (optional)
        // Remove this if you want to keep the full JSON in Loki
        stage.output {
            source = "msg"
        }
    
        forward_to = [loki.write.default.receiver]
    }
    
    // 3. WRITE TO LOKI
    loki.write "default" {
        endpoint {
            url = "http://loki.internal:3100/loki/api/v1/push"
    
            // If Loki requires auth:
            // basic_auth {
            //   username = "user"
            //   password = env("LOKI_PASSWORD")
            // }
        }
    }
    ```
   
    **Replace `loki.internal` with the corresponding hostname where Loki is running.**
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
