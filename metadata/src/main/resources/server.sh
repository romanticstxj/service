#!/bin/bash
source /etc/profile

BASE_DIR=/services/apps/service-metadata
#SYSTEM_LOG=/services/logs/service-metadata/log.txt
SYSTEM_LOG=/dev/null
SERVER_NAME="com.madhouse.platform.premiummad.task.MainTask"
STARTUP_CLASS="com.madhouse.platform.premiummad.task.MainTask"
CHECK_PID=`ps -ef|grep $SERVER_NAME |grep -v grep | awk '{print $2}' `

#JVM
export JMX_PORT=0912
export CLASSPATH=$BASE_DIR/conf:$(ls $BASE_DIR/*.jar | tr '\n' :)

#UEAP jvm args
UEAP_JVM_ARGS=" -Xms500m -Xmx500m -Xss256k 
-XX:MaxMetaspaceSize=128m 
-XX:MetaspaceSize=80m 
-XX:MaxDirectMemorySize=256m 
-XX:InitialCodeCacheSize=40M 
-XX:ReservedCodeCacheSize=50M 
-XX:+UseParNewGC 
-XX:+UseConcMarkSweepGC 
-XX:CMSInitiatingOccupancyFraction=75 
-XX:+UseCMSInitiatingOccupancyOnly -XX:+DisableExplicitGC"
UEAP_JVM_ARGS="$UEAP_JVM_ARGS -cp $CLASSPATH -Dueap.home=$ueap_home -Dcollect.start.worker=false"

status(){
	if [  -z  $CHECK_PID ];then
		echo "Service is not Running."
	else
		echo "Service is Running."
	fi
	
}

start(){
	if [ ! -z $CHECK_PID ];then
		echo "Service is  Running,Start failed."
		exit 1
	fi

    echo "BASE_DIR is :"$BASE_DIR
    echo "see result at:"$SYSTEM_LOG
    nohup java $UEAP_JVM_ARGS  -Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false \
-Dcom.sun.management.jmxremote.port=$JMX_PORT $STARTUP_CLASS > $SYSTEM_LOG & 
    sleep 1;
    exit
}

stop(){
    if [ -z $CHECK_PID ];then
	echo "Service is not Running,Stop failed"
	exit 1
    fi

    kill -9 $CHECK_PID
    echo "service Stop Successfully"
}

case $1 in
	start)
        start ;
        ;;
	stop)
        stop ;
        ;;
	status)
	  status ;
	;;
	test)
	  echo $CHECK_PID ;
	;;
        *)
        printf "Usage: %s{start|stop|status}\n"
        exit 1
        ;;
esac
