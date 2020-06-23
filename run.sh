#JAVA_HOME=/java/jdk1.8.0_161/bin
JAVA_HOME=`which java`
#echo $JAVA_HOME
nacos_addr=127.0.0.1:8848
active_profile=dev
if [ ${1} ];
then
  nacos_addr=$1
fi

if [ ${2} ];
then
  active_profile=$2
fi

PID=$(ps -ef | grep api-gateway-server-1.0-SNAPSHOT.jar | grep -v grep | awk '{ print $2 }')
if [ ${PID} ];
then
 kill -9 $PID
else
 echo 'api-gateway-server Application is already stopped...'
fi
nohup $JAVA_HOME  -Xmx3g -Xms3g -Xmn1g -Xss256k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -jar api-gateway-server-1.0-SNAPSHOT.jar --spring.profiles.active=$active_profile --spring.cloud.nacos.discovery.server-addr=$nacos_addr --spring.cloud.nacos.config.server-addr=$nacos_addr >/dev/null 2>&1 &
echo 'success start api-gateway-server-1.0-SNAPSHOT.jar'

PID=$(ps -ef | grep oauth-server-1.0-SNAPSHOT.jar | grep -v grep | awk '{ print $2 }')
if [ ${PID} ];
then
 kill -9 $PID
else
 echo 'oauth-server Application is already stopped...'
fi
nohup $JAVA_HOME  -Xmx3g -Xms3g -Xmn1g -Xss256k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -jar oauth-server-1.0-SNAPSHOT.jar --spring.profiles.active=$active_profile --spring.cloud.nacos.discovery.server-addr=$nacos_addr --spring.cloud.nacos.config.server-addr=$nacos_addr >/dev/null 2>&1 &
echo 'success start oauth-server-1.0-SNAPSHOT.jar'


PID=$(ps -ef | grep file-server-1.0-SNAPSHOT.jar | grep -v grep | awk '{ print $2 }')
if [ ${PID} ];
then
 kill -9 $PID
else
 echo 'oauth-server Application is already stopped...'
fi
nohup $JAVA_HOME  -Xmx3g -Xms3g -Xmn1g -Xss256k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -jar file-server-1.0-SNAPSHOT.jar --spring.profiles.active=$active_profile --spring.cloud.nacos.discovery.server-addr=$nacos_addr --spring.cloud.nacos.config.server-addr=$nacos_addr >/dev/null 2>&1 &
echo 'success start file-server-1.0-SNAPSHOT.jar'
echo 'YDH start complete'
