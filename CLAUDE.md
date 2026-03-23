# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

Apache Calcite 教程项目，通过渐进式示例演示 Calcite 的核心能力：SQL 解析、校验、SqlNode 到 RelNode 转换等。基于 Calcite 1.40.0，Java 8。

## 构建命令

```bash
# 全量构建
mvn clean install

# 构建单个模块（如 tutorial01，需先构建依赖模块）
mvn clean install -pl tutorials/tutorial01 -am

# 运行全部测试
mvn test

# 运行单个测试类
mvn test -pl tutorials/tutorial01 -Dtest=CalciteSqlParserTest

# 运行单个测试方法
mvn test -pl tutorials/tutorial01 -Dtest=CalciteSqlParserTest#defaultParse
```

## 模块架构

```
calcite-bom/       → BOM 模块，统一管理所有依赖版本（所有子模块 parent 指向此模块）
calcite-util/      → 工具库，提供 SqlNodeTreePrintVisitor（SqlNode 树打印）、ResultSetFormatter 等
tutorials/
  quickstart/      → CSV 适配器示例 + 内存 Schema（HrSchema）+ MySQL JDBC 连接
  tutorial01/      → SQL 解析专题：标准/Babel/DDL 解析器、SqlNode↔SQL 互转、自定义 Parser 扩展（FMPP+JavaCC 代码生成）
  tutorial02/      → SQL 校验 + SqlNode→RelNode 转换（依赖外部 MySQL 实例）
catalogs/mysql/    → 测试用 MySQL Docker 环境（Dockerfile + 数据集导入脚本）
```

## 关键架构细节

- **依赖继承**：根 `pom.xml` 仅聚合模块，`calcite-bom/pom.xml` 才是各子模块的 parent，负责 dependencyManagement
- **tutorial01 代码生成**：通过 `src/main/codegen/` 下的 FMPP 配置 + FreeMarker 模板 + JavaCC 生成自定义 SQL Parser。构建流程：`maven-resources-plugin` → `drill-fmpp-maven-plugin` → `javacc-maven-plugin`，生成代码在 `target/generated-sources/`
- **tutorial02 外部依赖**：测试需要运行中的 MySQL 实例，连接信息通过环境变量 `MYSQL_BASE_URL`（默认 `localhost:3306`）配置，用户名 `calcite`，数据库 `test`

## 技术栈

- 测试框架：JUnit 5 + Lombok（`@SneakyThrows` 简化异常处理）
- 日志：SLF4J + Logback
- 其他：Guava、Immutables（`@Value.Immutable` 用于生成不可变对象）、Protobuf
