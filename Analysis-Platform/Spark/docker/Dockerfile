FROM registry.cn-hangzhou.aliyuncs.com/hzgc/spark:2.2.0-hadoop-2.7

COPY spark.jar /
COPY start-kafka-to-parquet.sh /
COPY start-kafka-to-tidb.sh /

RUN chmod +x /start-kafka-to-parquet.sh && chmod +x /start-kafka-to-tidb.sh