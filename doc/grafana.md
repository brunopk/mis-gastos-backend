# Grafana

Follow these steps to recreate dashboards in Grafana : 

1. Create the *Datasource* (*Main Menu* > *Connections* > *Datasource*) : 
    Datasource type: MySQL
    Name: misgastos

    > Important: name must be **misgastos** as using another name may break graphics. If necessary it's possible to set a different database name associated to the same Datasource.
2. Import dashboards in the [`/grafana`](/grafana) folder. 

    > Do not use dashboards from [`/grafana/backup`](/grafana/backup), this folder contains old versions.