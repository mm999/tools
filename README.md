# tools-纯原创，可复用的工具类或常用的简单功能例子
`java` `网络编程` `Netty` `多线程` `爬虫`  

# 目录  

1 [集合](#集合类工具)  
>1.1 [同步FIFO队列](#同步fifo队列)  

2 [资源生成](#资源生成)  
>2.1 [根据数据库表生成Mybatis的mapper.xml/dao/Po](#com)  

3 [编程方式访问Http资源](#com)  
>3.1 [Http发送Post请求](#com)  
  
4 [Socket](#socket)  
>4.1 [Java.io.Socket](#java.io.socket)  
>4.2 [Netty带心跳重连机制的服务端](#netty带心跳重连机制的服务端)  
>4.3 [Netty带心跳重连机制的客户端](#netty带心跳重连机制的客户端)

## 集合类工具
  包路径:
  ```java
  package com.xiafei.tools.collections;
  ```

### 同步FIFO队列  
  同步-基于数组实现的FIFO队列，比java库自带阻塞队列轻量级。  

## 资源生成  
   包路径:
   ```java
   package com.xiafei.tools.generatesource;
   ```
###  根据数据库表生成Mybatis的mapper.xml/dao/Po  

   * 目前只支持Mysql数据的资源生成
   * 支持PO、Mapper、Dao的增量生成、覆盖、部分生成（只生成Dao或只生成Mapper）  
   
## 使用编程方式访问http资源  

   包路径:
  ```java
  package com.xiafei.tools.httpclient;
  ```

### Http发送Post请求  
   
   * 将数据放入Form-Data中的方式
   * 将数据封装成Json串的方式
  
## Socket
### Java.io.Socket
### Netty带心跳重连机制的服务端
### Netty带心跳重连机制的客户端
**未完成，待续**
