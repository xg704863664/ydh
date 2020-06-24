#!/bin/bash

source ./config.sh

if [ ${1} ];
then
  nacos_ip_port=$1
fi

if [ ${2} ];
then
  active_profile=$2
fi

./stop.sh
./start.sh ${nacos_ip_port} ${active_profile}

echo 'Successfully restarted YDH...'
