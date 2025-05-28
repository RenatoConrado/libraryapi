# Criação do container

## 1. Network

```shell
docker network create library-network
```

## 2. postgres

```shell
docker run --name librarydb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=library --network library-network -d postgres
```

## 3. pgadmin

Obrigatório ser `host:80`

```shell
docker run --name pgadmin -p 5431:80 -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=admin --network library-network -d dpage/pgadmin4
```
