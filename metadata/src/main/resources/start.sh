#!/bin/sh
echo "start metadata start ..."

nohup java -jar ../metadata.jar > /dev/null 2>/services/logs/service-metadata/log.txt &