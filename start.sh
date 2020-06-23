#!/bin/bash
source ./config.sh

JAVA_HOME=`which java`

if [ ${1} ];
then
  nacos_ip_port=$1
fi

if [ ${2} ];
then
  active_profile=$2
fi

function start() {
    nohup $JAVA_HOME  -Xmx3g -Xms3g -Xmn1g -Xss256k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -jar $1 --spring.profiles.active=$active_profile --spring.cloud.nacos.discovery.server-addr=$nacos_ip_port --spring.cloud.nacos.config.server-addr=$nacos_ip_port >/dev/null 2>&1 &
    echo 'Successfully started '$2
}

for (( i = 0; i < ${#jar[@]}; ++i )); do
    jar_name=${jar[$i]%-*}
    server_name=${jar_name%-*}
    start ${jar[$i]} ${server_name}
done

echo 'Successfully started YDH...'
