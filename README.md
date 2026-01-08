## Insett Ecommerce Blueprint App

### Get it started
> cp .env.example .env

### Services start

TODO: by script ansible/terraform/init.sh

#### dev
```bash
docker compose -f ./docker-compose.dev.yml \
up -d --build --force-recreate --remove-orphans
```

```bash
docker compose -f elasticsearch/docker-compose-elastic.yml \
up -d --build --force-recreate --remove-orphans
```