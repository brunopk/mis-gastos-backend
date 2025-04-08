#!/usr/bin/with-contenv bashio

# TODO: reimplement for Java

echo Node version: $(node --version)

echo Installing node packages
cd dist
npm install

echo Starting server
node app.js