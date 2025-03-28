#!/usr/bin/with-contenv bashio

echo Node version: $(node --version)

echo Installing node packages
cd dist
npm install

echo Starting server
node app.js