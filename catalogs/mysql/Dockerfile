FROM mysql:latest

# 设置环境变量
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=test
ENV MYSQL_USER=calcite
ENV MYSQL_PASSWORD=apache#calcite

# 创建目录用于存放导入脚本和数据文件
RUN mkdir -p /docker-entrypoint-initdb.d/import-scripts
RUN mkdir -p /docker-entrypoint-initdb.d/data-files

# 复制导入脚本到容器中
COPY import-data.sh /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/import-data.sh

# 复制 schema 文件到容器中
COPY ./dataset/schema /docker-entrypoint-initdb.d/import-scripts

# 复制文件到容器中, 并解压
ADD ./dataset/data/data.tar.gz /var/lib/mysql-files/data-files

# 暴露 MySQL 端口
EXPOSE 3306