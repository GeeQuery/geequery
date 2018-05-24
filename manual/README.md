# GeeQuery v2 user guide

[TOC]



# 1. Introduction

## 1.1 Background

## 1.2 Principles

# 2. Quick start

## 2.1 First Application

Here we will create an application with  Spring-boot and Spring-data

pom.xml

```java


```










# 3. Supported JPA Features

# 4. Supported QueryDSL Features 



2. Tutorials






# Anniox Roadmap

Roadmap of EF 2



* About Data sharding 
* Condition Creatria Stynx
* QueryDSL Intergation
* POJO
* No Pooling
* Move package to com.github
* Query-DSL with SQL/JPA
* Single DataSource. One session factory . one datasource.
* how to switch datasoruce and lookup bwtween multiple tables. 



要不要向JPA EntityManager的API走一次

* QueryDSL API —— JPA（高）
* QueryDSL API —— SQL （高，是采用原生还是增加解析兼容处理待定）
* QueryDSL API —— JPASQL(？)
* Spring-Data API(高)
* Spring-Data的 QueryDslPredicateExecutor(高)
* Spring-Data的 JpaSpecificationExecutor(？)
* 事务管理方案选择
  * JPA Transaction
  * DataSource Transaction (ok)
  * 编写专门的事务管理器