#!/usr/bin/with-contenv bashio

echo Java version: $(java --version)

echo Starting server
java -jar mis-gastos-backend.jar
