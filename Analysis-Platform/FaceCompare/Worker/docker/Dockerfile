#指定镜像
FROM registry.cn-hangzhou.aliyuncs.com/hzgc/gs-facecompare:1.0

#设置环境变量
#ENV LD_LIBRARY_PATH=/opt/GsFaceLib/face_libs

#操作文件
COPY worker /worker

#执行命令
RUN chmod +x /worker/bin/* &&  /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#初始化参数并启动应用
ENTRYPOINT ["/bin/sh","-c","/worker/bin/start-worker.sh"]