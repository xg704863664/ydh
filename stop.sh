#!/bin/bash

source ./config.sh

function stop(){
    PID=$(ps -ef | grep $1 | grep -v grep | awk '{ print $2 }')
    if [ ${PID} ];
    then
     kill -9 $PID
    else
     echo $2 ' is already stopped.'
    fi
}

for (( i = 0; i < ${#jar[@]}; ++i )); do
    jar_name=${jar[$i]%-*}
    server_name=${jar_name%-*}
    stop ${jar[$i]} ${server_name}
done

echo 'Stop YDH Successfully...'
