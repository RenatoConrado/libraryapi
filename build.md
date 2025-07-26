# 1. Build Image

```
docker build --tag renatoconrado/libraryapi .
```

## 2. Network

```shell
docker network create library-network
```

# 3. Build Container

```shell
docker run --name libraryapi-production --network library-network -d -p 8080:8080 -p 9090:9090 renatoconrado/libraryapi
```

# 3. run

```shell
sudo docker run --name libraryapi -p 8080:8080 -p 9090:9090 renatoconrado/libraryapi
```