#!/bin/bash

# TODO: update build script to generate JWT_SECRET with openssl rand -base64 256

# TODO: update build script to generate GOOGLE_TOKEN_ENCRYPTION_SECRET with openssl rand -base64 32


# Cross-platform sed replace
sed_replace() {
  local search="$1"
  local replace="$2"
  local file="$3"

  if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS (BSD sed needs -i '')
    sed -i '' "s|$search|$replace|" "$file"
  else
    # Linux (GNU sed)
    sed -i "s|$search|$replace|" "$file"
  fi
}

DB_HOSTNAME=$1
DB_NAME=$2
DB_USER=$3
DB_PASS=$4

echo "📁 Creating build/ directory"
mkdir -p build

echo "⚙️ Copying src/main/resources/application-prod.yaml"
[ -e src/main/resources/application-prod.yaml ] && cp src/main/resources/application-prod.yaml build/

echo "⚙️ Copying config.yaml"
[ -e config.yaml ] && cp config.yaml build/

echo "💲Copying run.sh"
[ -e run.sh ] && cp run.sh build/

echo "🐳 Generating Dockerfile"
[ -e Dockerfile ] && cp Dockerfile build/
sed_replace "ARG DB_USER=your_user" "ARG DB_USER=$DB_USER" build/Dockerfile
sed_replace "ARG DB_PASS=your_password" "ARG DB_PASS=$DB_PASS" build/Dockerfile
sed_replace "ARG DB_URL=jdbc:mariadb://localhost:3306/db_name" "ARG DB_URL=jdbc:mariadb://$DB_HOSTNAME:3306/$DB_NAME?serverTimezone=UTC" build/Dockerfile

echo "🖼️ Copying icon.png"
[ -e icon.png ] && cp icon.png build/

echo "ℹ️ Copying README.md"
[ -e README.md ] && cp README.md build/

echo "🛠️ Building"
mvn package && cp target/mis-gastos-backend-0.0.1.jar build/mis-gastos-backend.jar
