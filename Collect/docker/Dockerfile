#指定镜像
FROM cq.xuduan.tech:60419/hzgc/cuda8-java8:1.0

#操作文件
COPY docker-entrypoint.sh /
COPY collect-ftp.jar /app.jar

#执行命令
RUN chmod +x /docker-entrypoint.sh
COPY /

#初始化参数并启动Springboot应用
ENTRYPOINT ["/bin/sh","docker-entrypoint.sh"]


