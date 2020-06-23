#!/bin/bash
# 停止怎么办?
# 判断进程是否存在时,封装成一个函数


usage() {
  # the configfile will be properly formatted as long as the
  # configfile path is less then 40 chars, otw the line will look a
  # bit weird, but otherwise it's fine
  printf "usage: $0 <parameters>
  Optional parameters:
     -h                                                    Display this message
     --help                                                Display this message
     --configfile=%-40s ZooKeeper config file
     --myid=#                                              Set the myid to be used, if any (1-255)
     --force                                               Force creation of the data/txnlog dirs
" "$ZOOCFG"
  exit 1
}

if [ $? != 0 ] ; then
    usage
    exit 1
fi




nacos_ip_port=127.0.0.1:8848
active_profile=dev
api_gateway_server_name=api-gateway-server-1.0-SNAPSHOT.jar
oauth_server_name=oauth-server-1.0-SNAPSHOT.jar
file_server_name=file-server-1.0-SNAPSHOT.jar



JAVA_HOME=`which java`
if [ ${1} ];
then
  nacos_ip_port=$1
fi

if [ ${2} ];
then
  active_profile=$2
fi

# Start api gateway server
PID=$(ps -ef | grep $api_gateway_server_name | grep -v grep | awk '{ print $2 }')
if [ ${PID} ];
then
 kill -9 $PID
else
 echo 'Api gateway server is already stopped.'
fi

nohup $JAVA_HOME  -Xmx3g -Xms3g -Xmn1g -Xss256k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -jar $api_gateway_server_name --spring.profiles.active=$active_profile --spring.cloud.nacos.discovery.server-addr=$nacos_ip_port --spring.cloud.nacos.config.server-addr=$nacos_ip_port >/dev/null 2>&1 &
echo 'Successfully started api gateway server.'


# Start oauth server
PID=$(ps -ef | grep $oauth_server_name | grep -v grep | awk '{ print $2 }')
if [ ${PID} ];
then
 kill -9 $PID
else
 echo 'Oauth server is already stopped.'
fi


nohup $JAVA_HOME  -Xmx3g -Xms3g -Xmn1g -Xss256k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -jar $oauth_server_name --spring.profiles.active=$active_profile --spring.cloud.nacos.discovery.server-addr=$nacos_ip_port --spring.cloud.nacos.config.server-addr=$nacos_ip_port >/dev/null 2>&1 &
echo 'Successfully started oauth server.'


# Start file server
PID=$(ps -ef | grep $file_server_name | grep -v grep | awk '{ print $2 }')
if [ ${PID} ];
then
 kill -9 $PID
else
 echo 'File server is already stopped.'
fi


nohup $JAVA_HOME  -Xmx3g -Xms3g -Xmn1g -Xss256k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -jar $file_server_name --spring.profiles.active=$active_profile --spring.cloud.nacos.discovery.server-addr=$nacos_ip_port --spring.cloud.nacos.config.server-addr=$nacos_ip_port >/dev/null 2>&1 &
echo 'Successfully started file server.'

##################################################
# If we have the other server, add the code here.



##################################################


echo 'Successfully started YDH...'
