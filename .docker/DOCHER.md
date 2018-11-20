# Docker基础教程

## 1、Docker 简介 官网地址 [https://www.docker.com](https://www.docker.com)

###1.1、什么是Docker？
Docker是一个开源的引擎，可以轻松的为任何应用创建一个轻量级的、可移植的、自给自足的容器。开发者在笔记本上编译测试通过的容器可以批量地在生产环境中部署，包括VMs（虚拟机）、bare metal、OpenStack 集群和其他的基础应用平台。

###1.2、Docker通常用于如下场景：

- web应用的自动化打包和发布；
- 自动化测试和持续集成、发布；
- 在服务型环境中部署和调整数据库或其他的后台应用；
 - 从头编译或者扩展现有的OpenShift或Cloud Foundry平台来搭建自己的PaaS环境。

###1.3、Docker用途：
- 它使你能够将你的应用程序从基础架构中分离，从而可以快速交付。
- 使用Docker，你可以与管理应用程序相同的方式来管理这些基础架构。
- 使用Docker的方法，进行快速开发，测试，并可以显著的减少编写代码和运行之间的时间延迟。


##2、 CentOS Docker 安装

###2.1、 Docker支持以下版本CentOS版本

目前，CentOS 仅发行版本中的内核支持 Docker。

- Docker 运行在 CentOS 7 上，要求系统为64位、系统内核版本为 3.10 以上。

- Docker 运行在 CentOS-6.5 或更高的版本的 CentOS 上，要求系统为64位、系统内核版本为 2.6.32-431 或者更高版本。

###2.2、 yum 安装
Docker 要求 CentOS 系统的内核版本高于 3.10.具体操作如下：
``` bash
# 查看你当前的内核版本
uname -r

# 安装必要的系统工具
sudo yum install -y yum-utils device-mapper-persistent-data lvm2

# 添加软件源信息
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 更新 yum 缓存
sudo yum makecache fast

# 安装 docker-ce
sudo yum -y install docker-ce

# 启动
systemctl start docker

# 测试运行 hello-world
docker run hello-world

# 重启
systemctl restart docker

# 查看状态
systemctl status docker

# 开机自启动
systemctl enable docker

# 禁止开机自启动
systemctl disable docker
```
###2.3、 docker 基础命令
#### docker 命令帮助
##### docker --help

|Commands|desc|cn-desc|
|:---:|:---|:---|
    |attach |   Attach to a running container     |            # 当前 shell 下 attach 连接指定运行镜像|
   | build    |  Build an image from a Dockerfile        |       # 通过 Dockerfile 定制镜像|
    |commit  |   Create a new image from a container's changes | # 提交当前容器为新的镜像|
   | cp      |   Copy files/folders from the containers filesystem to the host path|      # 从容器中拷贝指定文件或者目录到宿主机中|
   | create   | Create a new container              |           # 创建一个新的容器，同 run，但不启动容器|
   | diff     |  Inspect changes on a container's filesystem  |  # 查看 docker 容器变化|
   | events  |   Get real time events from the server       |    # 从 docker 服务获取容器实时事件|
   | exec     |  Run a command in an existing container   |     # 在已存在的容器上运行命令|
   | export   | Stream the contents of a container as a tar archive |       # 导出容器的内容流作为一个 tar 归档文件[对应 import ]|
   | history  | Show the history of an image                 | # 展示一个镜像形成历史|
   | images   | List images               |                    # 列出系统当前镜像|
   | import   | Create a new filesystem image from the contents of a tarball |   # 从tar包中的内容创建一个新的文件系统映像[对应 export]|
   | info    |  Display system-wide information           |    # 显示系统相关信息|
   | inspect |  Return low-level information on a container  | # 查看容器详细信息|
   | kill    |  Kill a running container                    |  # kill 指定 docker 容器|
|    load   |   Load an image from a tar archive           |   # 从一个 tar 包中加载一个镜像[对应 save]|
|   login   |  Register or Login to the docker registry server  |  # 注册或者登陆一个 docker 源服务器|
|   logout  |  Log out from a Docker registry server      |   # 从当前 Docker registry 退出|
|   logs    |  Fetch the logs of a container             |    # 输出当前容器日志信息|
 |  port    |  Lookup the public-facing port which is NAT-ed to PRIVATE_PORT   |   # 查看映射端口对应的容器内部源端口|
   | pause   |  Pause all processes within a container     |   # 暂停容器|
   | ps      |  List containers                              | # 列出容器列表|
   | pull    |  Pull an image or a repository from the docker registry server  |    # 从docker镜像源服务器拉取指定镜像或者库镜像|
   | push    |  Push an image or a repository to the docker registry server    |    # 推送指定镜像或者库镜像至docker源服务器|
   | restart |  Restart a running container           |        # 重启运行的容器|
|    rm      |  Remove one or more containers        |         # 移除一个或者多个容器|
|    rmi     |  Remove one or more images            |         # 移除一个或多个镜像[无容器使用该镜像才可删除，否则需删除相关容器才可继续或 -f 强制删除]|
|    run     |  Run a command in a new container    |          # 创建一个新的容器并运行一个命令|
|    save    |  Save an image to a tar archive       |         # 保存一个镜像为一个 tar 包[对应 load]|
|    search  |  Search for an image on the Docker Hub    |     # 在 docker hub 中搜索镜像|
|    start   |  Start a stopped containers             |       # 启动容器|
|    stop    |  Stop a running containers              |       # 停止容器|
 |   tag     |  Tag an image into a repository          |      # 给源中镜像打标签|
  |  top     |  Lookup the running processes of a container|   # 查看容器中运行的进程信息|
  |  unpause |  Unpause a paused container             |       # 取消暂停容器|
   | version  | Show the docker version information       |    # 查看 docker 版本号|
   | wait     | Block until a container stops, then print its exit code  |  # 截取容器停止时的退出状态值|
Run 'docker COMMAND --help' for more information on a command.
  

### 3、docker 安装 mysql 5.7 为例， 讲解 docker 基础命令


#### 3.1、 docker run 安装方式

##### 3.1.1、查找Docker Hub上的mysql镜像
####### docker search mysql
|NAME|DESCRIPTION|STARS|OFFICIAL|AUTOMATED|
|:---:|:---:|:---:|:---:|:---:|
| mysql|MySQL is a widely used, open-source relation… |7266 |[OK]|    | 
|mariadb             |                                   MariaDB is a community-developed fork of MyS¡­  | 2338|                [OK]|   |
|mysql/mysql-server |                                    Optimized MySQL Server Docker images. Create¡­   |535|                      |               [OK]|
|zabbix/zabbix-server-mysql|                             Zabbix Server with MySQL database support       |137|                        |             [OK]|
|hypriot/rpi-mysql |                                     RPi-compatible Docker Image with Mysql          |97|                         |                   |       
|zabbix/zabbix-web-nginx-mysql|                          Zabbix frontend based on Nginx web-server wi¡­   |74 |                        |             [OK]|
|centurylink/mysql|                                      Image containing mysql. Optimized to be link¡­  | 59|                         |             [OK]|


##### 3.1.2、拉取官方镜像，以标签为: 5.7， 查看本地镜像
####### docker pull mysql：5.7
#######  docker images |grep mysql
|REPOSITORY|TAG|IMAGE ID|CREATED|SIZE|
|:---:|:---:|:---:|:---:|:---:|
|mysql                                 |         5.7             |    702fb0b7837f    |    12 days ago      |   372MB|
##### 结果集说明:
     REPOSITORY  镜像的仓库源
     TAG         镜像标签
     MAGE ID     镜像ID
     CREATED     最近创建镜像时间
     SIZE        镜像大小
##### 3.1.3、运行容器:
####### docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
#######   docker run -p 3306:3306 --name mysqldb \
######   -v $PWD/conf:/etc/mysql/conf.d \
######   -v $PWD/logs:/logs \
######   -v $PWD/data:/var/lib/mysql \
 ######    -e MYSQL_ROOT_PASSWORD=123456 \ 
 ######    -d mysql:5.7
 ###### 21cb89213c93d805c5bacf1028a0da7b5c5852761ba81327e6b99bb3ea89930e

##### OPTIONS命令说明：

- -p 3306:3306：将容器的 3306 端口映射到主机的 3306 端口。

- -v -v $PWD/conf:/etc/mysql/conf.d：将主机当前目录下的 conf/my.cnf 挂载到容器的 /etc/mysql/my.cnf。

- -v $PWD/logs:/logs：将主机当前目录下的 logs 目录挂载到容器的 /logs。

- -v $PWD/data:/var/lib/mysql ：将主机当前目录下的data目录挂载到容器的 /var/lib/mysql 。

- -e MYSQL_ROOT_PASSWORD=123456：初始化 root 用户的密码。


##### 3.1.4、查看容器的运行情况
####### docker ps [OPTIONS]

##### OPTIONS说明：

- -a :显示所有的容器，包括未运行的。

- -f :根据条件过滤显示的内容。

- --format :指定返回值的模板文件。

- -l :显示最近创建的容器。

- -n :列出最近创建的n个容器。

- --no-trunc :不截断输出。

- -q :静默模式，只显示容器编号。

- -s :显示总的文件大小。

#######  docker ps -l
 |CONTAINER ID      |  IMAGE     |                         COMMAND     |             CREATED         |    STATUS      |        PORTS           |                    NAMES|
 |:---:|:---:|:---:|:---:|:---:|:---:|:---:|
 |d2af0970869b     |   172.18.18.122/mysql/mysql:5.7.19  | "docker-entrypoint.s¡­"  | 19 hours ago      |  Up 19 hours    |     0.0.0.0:3306->3306/tcp |             mysqldb|

##### 结果集说明:
     CONTAINER ID   容器ID
     IMAGE          镜像
     COMMAND        镜像ID
     CREATED        创建时间
     STATUS         状态
     PORTS          开放端口
     NAMES          容器名称
     
##### 3.1.5、查看容器的日志  
#######   docker logs [OPTIONS] CONTAINER
##### OPTIONS 说明：
- -f : 跟踪日志输出
- --since :显示某个开始时间的所有日志
- -t : 显示时间戳
- --tail :仅列出最新N条容器日志
#######   docker logs -f -t  d2af0970869b     docker logs -f -t  mysqldb     



##### 3.1.6、容器启动/停止/重启  
#######   docker  start/stop/restart [OPTIONS] CONTAINER [CONTAINER...]
#######   docker start d2af0970869b   
#######   docker stop d2af0970869b   
#######   docker restart d2af0970869b 


#####  在运行的容器中执行命令  
####### docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
##### OPTIONS说明：
- -d :分离模式: 在后台运行
- -i :即使没有附加也保持STDIN 打开
- -t :分配一个伪终端
#######   docker exec -it d2af0970869b  


#### 3.2、 docker-compose 方式安装mysql
#### docker-compose 简介

- docker-compose 是用来做docker 的多容器控制
- docker-compose 可以把所有繁复的 docker 操作全都一条命令，自动化的完成。
- 用通俗的语言来说，我们平时操作 docker 还是很原始的一系列动作，你就要做更多的 docker 操作， 这显然是非常没有效率的。

#### 3.2.1、 docker-compose安装
##### 安装方式一：
##### curl -L https://github.com/docker/compose/releases/download/1.8.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
##### chmod +x /usr/local/bin/docker-compose

##### 安装方式二：
#####  yum install python-pip python-dev
#####  pip install docker-compose

##### 安装方式三：
#####  curl -L https://github.com/docker/compose/releases/download/1.8.0/run.sh > /usr/local/bin/docker-compose
#####  chmod +x /usr/local/bin/docker-compose

##### 安装方式四：
#####  wget https://github.com/docker/compose/releases/download/1.8.1/docker-compose-Linux-x86_64
#####  mv docker-compose-Linux-x86_64 /usr/local/bin/docker-compose;

##### 查看docker-compose版本：
#####  docker-compose --version



#### 3.2.2、 编写 docker-compose.yml
##### vim docker-compose.yml
```
version: '2.3'
services:
  mysqldb:
    image: 172.18.18.122/mysql/mysql:5.7.19
    container_name: mysqldb
    restart: always
    volumes:
    - ./mysqldb:/var/lib/mysql
    - ./config/my.cnf:/etc/my.cnf
    - ./init:/docker-entrypoint-initdb.d/
    - /etc/localtime:/etc/localtime:ro
    restart: always
    environment:
    - SET_CONTAINER_TIMEZONE=true
    - TZ=Asia/ShangHai
    - MYSQL_ROOT_PASSWORD=root
    - MYSQL_DATABASE=lldb
    - MYSQL_USER=lldb
    - MYSQL_PASSWORD=lldb
    ports:
    - "3306:3306"
```

#### 3.2.3、 启动docker-compose.yml，进入与docker-compose.yml 同级目录下
##### docker-compose up -d

#### 3.2.4、 停止docker-compose.yml
##### docker-compose down -v

###4、Harbor安装 -- 企业级Registry仓库

#### 3.3、 dockerfile（以创建 nginx 为例）
#### 3.3.1、 创建目录
mkdir dockerfile && cd dockerfile
#### 3.3.2、 创建 Dockerfile 文件
vim Dockerfile
```
    FROM centos:7
    MAINTAINER liang
    RUN yum install epel-release -y
    RUN yum install nginx -y
    COPY index.html /usr/share/nginx/html
    ENTRYPOINT  ["nginx", "-g", "daemon off;"]
    EXPOSE 80
```

#### 3.3.3、构建 Dockerfile 创建容器
docker build -t nginx/nginx_dockfile .

#### 3.3.4、运行创建容器
docker run -p 80:80 -d nginx/nginx_dockfile .


#### 3.3.5、镜像分层
dockerfile的每一行都会产生一个新的层

#### 3.3.6、Dockerfile 命令
    命令 	    用途
    FROM 	    base image
    RUN 	    执行命令
    ADD 	    添加文件
    COPY 	    拷贝文件
    CMD 	    执行命令
    EXPOSE 	    暴露端口
    WORKDIR     指定路径
    ENV 	    指定环境变量
    ENTRYPINT   容器入口
    USER 	    指定用户
    VOLUME 	    指定挂载点

### 4、
#### 4.1、Harbor官方描述：
Harbor是一个用于存储和分发Docker镜像的企业级Registry服务器，通过添加一些企业必需的功能特性，例如安全、标识和管理等，扩展了开源Docker Distribution。作为一个企业级私有Registry服务器，Harbor提供了更好的性能和安全。提升用户使用Registry构建和运行环境传输镜像的效率。Harbor支持安装在多个Registry节点的镜像资源复制，镜像全部保存在私有Registry中， 确保数据和知识产权在公司内部网络中管控。另外，Harbor也提供了高级的安全特性，诸如用户管理，访问控制和活动审计等。

#### 4.2、Harbor下载：
#### wget https://github.com/vmware/harbor/releases/download/v1.1.2/harbor-offline-installer-v1.1.2.tgz

#### 4.3、解压 配置 harbor
##### 解压
tar zxvf harbor-offline-installer-v1.1.2.tgz

##### 4.4、进入目录下
cd harbor/

##### 4.5、修改配置文件
vim harbor.cfg
```angular2html
    ## Configuration file of Harbor 
    
    # hostname设置访问地址，可以使用ip、域名，不可以设置为127.0.0.1或localhost 
    hostname = 10.236.63.76 
    
    # 访问协议，默认是http，也可以设置https，如果设置https，则nginx ssl需要设置
    on ui_url_protocol = http 
    
    # mysql数据库root用户默认密码root123，实际使用时修改下 
    db_password = root123 
    max_job_workers = 3 
    customize_crt = on 
    ssl_cert = /data/cert/server.crt 
    ssl_cert_key = /data/cert/server.key 
    secretkey_path = /data 
    admiral_url = NA 
    
    # 邮件设置，发送重置密码邮件时使用 
    email_identity =
     email_server = smtp.mydomain.com 
    email_server_port = 25 
    email_username = sample_admin@mydomain.com
     email_password = abc 
     email_from = admin <5446543@mydomain.com> 
     email_ssl = false 
     
     # 启动Harbor后，管理员UI登录的密码，默认是Harbor12345 
     harbor_admin_password = Hzgc@123 
     
     # 认证方式，这里支持多种认证方式，如LADP、本次存储、数据库认证。默认是db_auth，mysql数据库认证 
     
     auth_mode = db_auth # LDAP认证时配置项 
     #ldap_url = ldaps://ldap.mydomain.com 
     #ldap_searchdn = uid=searchuser,ou=people,dc=mydomain,dc=com 
     #ldap_search_pwd = password 
     #ldap_basedn = ou=people,dc=mydomain,dc=com 
     #ldap_filter = (objectClass=person) 
     #ldap_uid = uid #ldap_scope = 3 
     #ldap_timeout = 5 
     
     # 是否开启自注册 
     self_registration = on 
     
     # Token有效时间，默认30分钟 
     token_expiration = 30 
     
     # 用户创建项目权限控制，默认是everyone（所有人），也可以设置为adminonly（只能管理员） 
     project_creation_restriction = 
     everyone verify_remote_cert = on

```
##### 4.6、编译环境
sh prepare
 
##### 执行安装
sh install.sh

##### 4.7、查看启动的服务
docker-compose ps

##### 4.8、现有harbor 
- 外网仓库地址 [http://cq.xuduan.tech:60419/harbor](http://cq.xuduan.tech:60419/harbor)
- 内网仓库地址 [http://172.18.18.122/harbor](http://172.18.18.122/harbor)

##### 4.9、基础命令
建项目完毕后，我们就可以用admin账户提交本地镜像到Harbor仓库了。例如我们提交本地 mysql 镜像：
```angular2html

# 官网下载 mysql 镜像
docker pull mysql:latest

# admin登录 
 docker login 10.236.63.76 
    Username: admin
     Password: 
Login Succeeded 

# 给镜像打tag 
 docker tag mysql:latest 172.18.18.122/mysql/mysql:5.7.19
 
# push到仓库 
 docker push 172.18.18.122/mysql/mysql:5.7.19

```
##### 4.10、启动Harbor
docker-compose down -v 

##### 4.11、停止Hharbor
 docker-compose up -d
