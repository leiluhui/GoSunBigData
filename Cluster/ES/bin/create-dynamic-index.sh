#!/bin/bash
################################################################################
## Copyright:    HZGOSUN Tech. Co, BigData
## Filename:     create-dynamic-index.sh
## Description:  创建动态库表的所有索引
## Author:       qiaokaifeng
## Created:      2017-11-28
################################################################################

#set -x
#---------------------------------------------------------------------#
#                              定义变量                               #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                   ### bin 目录
cd ..
ES_DIR=`pwd`                                    ### es 目录
cd ..
CLUSTER_DIR=`pwd`                               ### cluster 目录
cd ..
OBJECT_DIR=`pwd`                                ### Real 根目录
COMMON_DIR=${OBJECT_DIR}/common                 ### common 目录
CONF_FILE=${COMMON_DIR}/conf/project-conf.properties

#####################################################################
# 函数名: index_es_dynamic
# 描述: index_es的子函数，替换index-dynamic.sh中的节点名
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function index_es_dynamic()
{
    # 判断脚本是否存在，存在才执行
    if [ -f "${BIN_DIR}/index-dynamic.sh.templete" ]; then
        cp ${BIN_DIR}/index-dynamic.sh.templete ${BIN_DIR}/index-dynamic.sh

        ### 替换index-dynamic.sh中的节点名，共三处
        # 要替换的节点名，如s106
        ES_IP=$(grep es_service_node ${CONF_FILE} | cut -d '=' -f2)
        ES_Host=$(cat /etc/hosts|grep "$ES_IP" | awk '{print $2}')

        ## 第一处
        # 要查找的目标
        a1="curl -XDELETE '"
        b1="/dynamic?pretty'  -H 'Content-Type: application/json'"
        replace1="curl -XDELETE '${ES_Host}:9200/dynamic?pretty'  -H 'Content-Type: application/json'"
        # ^表示以什么开头，.*a表示以a结尾。替换以a1开头、b1结尾匹配到的字符串为repalce1
        sed -i "s#^${a1}.*${b1}#${replace1}#g" ${BIN_DIR}/index-dynamic.sh

        ## 第二处
        a2="curl -XPUT '"
        b2="/dynamic?pretty' -H 'Content-Type: application/json' -d'"
        replace2="curl -XPUT '${ES_Host}:9200/dynamic?pretty' -H 'Content-Type: application/json' -d'"
        sed -i "s#^${a2}.*${b2}#${replace2}#g" ${BIN_DIR}/index-dynamic.sh

        ## 第三处
        a3="curl -XPUT '"
        b3="/dynamic/_settings' -d '{"
        replace3="curl -XPUT '${ES_Host}:9200/dynamic/_settings' -d '{"
        sed -i "s#^${a3}.*${b3}#${replace3}#g" ${BIN_DIR}/index-dynamic.sh

		##获取分片、副本数
        Shards=$(grep Number_Of_Shards ${CONF_FILE} | cut -d '=' -f2)
        Replicas=$(grep Number_Of_Replicas ${CONF_FILE} | cut -d '=' -f2)
        ##获取模糊查询最小、最大字符数
        Min_Gram=$(grep Min_Gram ${CONF_FILE} | cut -d '=' -f2)
        Max_Gram=$(grep Max_Gram ${CONF_FILE} | cut -d '=' -f2)
        ##获取最大分页搜素文档数
        Max_Result_Window=$(grep Max_Result_Window ${CONF_FILE} | cut -d '=' -f2)

        sed -i "s#^\"min_gram\"#\"min_gram\":${Min_Gram}#g" ${BIN_DIR}/index-dynamic.sh
        sed -i "s#^\"max_gram\"#\"max_gram\":${Max_Gram}#g" ${BIN_DIR}/index-dynamic.sh
        sed -i "s#^\"number_of_shards\"#\"number_of_shards\":${Shards}#g" ${BIN_DIR}/index-dynamic.sh
        sed -i "s#^\"number_of_replicas\"#\"number_of_replicas\":${Replicas}#g" ${BIN_DIR}/index-dynamic.sh
        sed -i "s#^\"max_result_window\"#\"max_result_window\":${Max_Result_Window}#g" ${BIN_DIR}/index-dynamic.sh


        sh ${BIN_DIR}/index-dynamic.sh
		if [ $? = 0 ];then
			echo "修改index-dynamic.sh成功并执行......"  | tee  -a  $LOG_FILE
		fi
    else
        echo "index-dynamic.sh.templete不存在...."
    fi
}


#####################################################################
# 函数名: index_es_dynamic_show
# 描述: index_es的子函数，替换index-dynamicshow.sh中的节点名
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function index_es_dynamic_show()
{
    # 判断脚本是否存在，存在才执行
    if [ -f "${BIN_DIR}/index-dynamicshow.sh" ]; then

        ### 替换index-dynamicshow.sh中的节点名，共三处
        # 要替换的节点名，如s106
        ES_IP=$(grep es_service_node ${CONF_FILE} | cut -d '=' -f2)
        ES_Host=$(cat /etc/hosts|grep "$ES_IP" | awk '{print $2}')

        ## 第一处
        # 要查找的目标
        a1="curl -XDELETE '"
        b1="/dynamic?pretty'  -H 'Content-Type: application/json'"
        replace1="curl -XDELETE '${ES_Host}:9200/dynamic?pretty'  -H 'Content-Type: application/json'"
        # ^表示以什么开头，.*a表示以a结尾。替换以a1开头、b1结尾匹配到的字符串为repalce1
        sed -i "s#^${a1}.*${b1}#${replace1}#g" ${BIN_DIR}/index-dynamicshow.sh

        ## 第二处
        a2="curl -XPUT '"
        b2="/dynamic?pretty' -H 'Content-Type: application/json' -d'"
        replace2="curl -XPUT '${ES_Host}:9200/dynamic?pretty' -H 'Content-Type: application/json' -d'"
        sed -i "s#^${a2}.*${b2}#${replace2}#g" ${BIN_DIR}/index-dynamicshow.sh

        ## 第三处
        a3="curl -XPUT '"
        b3="/dynamic/_settings' -d '{"
        replace3="curl -XPUT '${ES_Host}:9200/dynamic/_settings' -d '{"
        sed -i "s#^${a3}.*${b3}#${replace3}#g" ${BIN_DIR}/index-dynamicshow.sh



        sh ${BIN_DIR}/index-dynamicshow.sh
		if [ $? = 0 ];then
			echo "修改index-dynamicshow.sh成功并执行......"  | tee  -a  $LOG_FILE
		fi
    else
        echo "index-dynamicshow.sh不存在...."
    fi
}

function main() {
    index_es_dynamic
    index_es_dynamic_show
}

main