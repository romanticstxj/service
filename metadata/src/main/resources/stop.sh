#!/bin/sh
echo "stop metadata"
pid=`ps -ef|grep "metadata"|grep -v "grep"|awk '{print $2}'`
if [ -z $pid ]; then
    echo "Warning:No this process."
else
    sudo kill -9 $pid
    echo "killed" $pid
fi