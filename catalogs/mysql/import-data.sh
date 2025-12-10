#!/bin/bash

# 等待 MySQL 服务启动
until mysqladmin ping -h localhost -u root -p"$MYSQL_ROOT_PASSWORD" --silent;
do
  echo "等待 MySQL 服务启动..."
  sleep 1
done

echo "MySQL 服务已启动，开始导入数据..."

# 导入 SQL 文件
for sql_file in /docker-entrypoint-initdb.d/import-scripts/*.sql;
do
  if [ -f "$sql_file" ]; then
    echo "导入 SQL 文件: $sql_file"
    mysql -h localhost -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" < "$sql_file"
  fi
done

# 导入 CSV 文件
for csv_file in /var/lib/mysql-files/data-files/*.csv;
do
  if [ -f "$csv_file" ]; then
    table_name=$(basename "$csv_file" .csv)
    echo "导入 CSV 文件: $csv_file 到表: $table_name"
    mysql -h localhost -u root -p"$MYSQL_ROOT_PASSWORD" -e "
      LOAD DATA INFILE '$csv_file' 
      INTO TABLE $table_name 
      FIELDS TERMINATED BY '|'
      ENCLOSED BY '\"' 
      LINES TERMINATED BY '\n';
    " "$MYSQL_DATABASE"
  fi
done

echo "数据导入完成！"
