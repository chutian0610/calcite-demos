# README

- 构建镜像

```shell
docker build -t my-mysql .
```

- 运行容器

```shell
docker run -d -p 3306:3306 --name my-mysql-container my-mysql
```
