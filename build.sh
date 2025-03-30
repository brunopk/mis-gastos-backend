#!/bin/bash

echo "📁 Creating dist/ directory"
mkdir -p dist
echo "💲 Copying .env"
[ -e .env ] && cp .env dist/
echo "💲 Copying .env.production"
[ -e .env.production ] && cp .env.production dist/
echo "🛠️  Building"
tsc