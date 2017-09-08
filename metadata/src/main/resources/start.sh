#!/bin/sh
echo "start metadata start ..."
export JAVA_HOME=/services/apps/jdk
export PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH

nohup java -jar ../metadata.jar > /dev/null 2>/services/logs/service-metadata/log.txt &