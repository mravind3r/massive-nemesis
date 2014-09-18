i#!/usr/bin/env bash

set -u -e

source /home/hwwetl/dbconf
source /usr/etl/HWW/common-scripts/init.sh
source /usr/etl/HWW/common-scripts/hive.sh
source /usr/etl/HWW/common-scripts/paths.cfg
source /usr/etl/HWW/common-scripts/driven.cfg

MODULE=casc-examples

hadoop jar $HWW_HOME/$MODULE/$MODULE-hadoop.jar \
  -Dmapred.job.queue.name=$HADOOP_QUEUE \
  -Dgraphite.host=$GRAPHITE_HOST \
  -Dgraphite.prefix=$GRAPHITE_PREFIX \

