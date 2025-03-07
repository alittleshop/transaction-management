# 基于官方的 OpenJDK 21 镜像作为基础镜像
FROM openjdk:21

# 设置工作目录
WORKDIR /app

# 将项目的 JAR 文件复制到容器的 /app 目录下
COPY target/transaction-management-1.0-SNAPSHOT.jar app.jar

# 暴露应用程序运行的端口（根据你的 Spring Boot 应用配置）
EXPOSE 8080

# 定义容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]