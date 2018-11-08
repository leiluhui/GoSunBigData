#!/bin/bash

spark-submit \
--master local[*] \
--class com.hzgc.cluster.spark.consumer.KafkaToTidb \
/spark.jar