#指定镜像
FROM 172.18.18.123/hzgc/java-all:8

#设置环境变量
ENV LD_LIBRARY_PATH=/opt/GsFaceLib/face_libs

#操作文件
COPY worker /worker

#执行命令
RUN chmod +x /worker/bin/*

#初始化参数并启动应用
ENTRYPOINT ["/bin/sh","-c","/worker/bin/start-worker.sh 1 aaa 9999 bbb"]