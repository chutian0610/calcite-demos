# README

- 构建镜像

```shell
docker build -t my-mysql .
```

- 运行容器

```shell
docker run -d -p 3306:3306 --name my-mysql-container my-mysql
```

- 连接数据库

注意测试环境要设置 allowPublicKeyRetrieval=true 才能连接数据库
