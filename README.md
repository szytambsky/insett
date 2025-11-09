## Insett Ecommerce Blueprint App

### Get it started
> cp .env.example .env

### Localstack with AWS Cdk Cli
```bash
  cd infrastracture/
```
```bash
  chmod +x ./localstack-deploy.sh \
  ./localstack-deploy.sh
```

### 🔌 Kafka connection

👉 **Dev (local)**  
Use in case connecting from host (`localhost`) by port **9094**

👉 **Stage / Prod (docker network)**  
Services are communicating inside internal pre-created 🐳 Docker network by hostname **kafka** and port **9092**:

### Services start

TODO: by script ansible/terraform/init.sh

#### dev
```bash
docker compose -f ./docker-compose.dev.yml \
up -d --build --force-recreate --remove-orphans
```
