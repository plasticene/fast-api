
FROM eclipse-temurin:8-jre

## 创建目录，并使用它作为工作目录
RUN mkdir -p /fast-api
WORKDIR /fast-api
## 将后端项目的 Jar 文件，复制到镜像中
COPY fast-api-service-0.0.1-SNAPSHOT.jar app.jar

## 设置 TZ 时区
## 设置 JAVA_OPTS 环境变量，可通过 docker run -e "JAVA_OPTS=" 进行覆盖
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms512m -Xmx512m"

## 暴露后端项目的 18888 端口
EXPOSE 18888

## 启动后端项目
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar app.jar
