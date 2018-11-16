#指定镜像
FROM cq.xuduan.tech:60419/hzgc/java-all:8

#设置环境变量
ENV PATH /usr/local/bin:$PATH

#操作文件
COPY docker-entrypoint.sh /
COPY dispatch.jar /app.jar

#执行命令
RUN chmod +x /docker-entrypoint.sh && /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#初始化参数并启动Springboot应用
ENTRYPOINT ["/bin/sh","docker-entrypoint.sh"]
