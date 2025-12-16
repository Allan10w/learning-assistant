#Dockerfile相当于应用打包的说明书，他告诉docker如何从零开始，把你的代码和运行环境封装成一个可独立运行的镜像
# 基于openjdk17，把编译好的JAR包复制进去，设定启动命令java -jar app.jar
#1.基于eclipse temurin openjdk 17版本
FROM eclipse-temurin:17-jdk-jammy
#2.把jenkins刚刚编译好的jar包复制进容器
COPY target/tlias-web-management-0.0.1-SNAPSHOT.jar app.jar
#3.设置启动命令：告诉容器启动时要执行什么命令
ENTRYPOINT ["java", "-jar", "/app.jar"]
