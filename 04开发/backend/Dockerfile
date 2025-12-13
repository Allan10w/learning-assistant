# 基于openjdk17，把编译好的JAR包复制进去，设定启动命令java -jar app.jar
FROM eclipse-temurin:17-jdk-jammy
COPY target/tlias-web-management-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
