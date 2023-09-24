# 指定基础镜像
FROM openjdk:11-jre

# 拷贝jdk和java项目的包
COPY ./sky-server-1.0-SNAPSHOT.jar /mydata/project/java/app.jar

# 暴露端口
EXPOSE 8080
# 入口，java项目的启动命令
ENTRYPOINT java -jar /mydata/project/java/app.jar
