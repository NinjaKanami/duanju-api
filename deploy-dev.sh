#!/bin/bash
env=$1
app_name=duanju-api
app_home=/home/www


PID=$(ps -ef | grep ${app_name}.jar | grep -v grep | awk '{ print $2}')

function app_stop()
{
        echo stopping application
        echo kill $PID
        kill -9 $PID
        echo stop application success

}

function app_start()
{
        java_opts="-Xms128m -Xmx128m \
                      -XX:+UseG1GC \
                      -XX:+HeapDumpOnOutOfMemoryError \
                      -XX:HeapDumpPath=dump.hprof \
                      -XX:+PrintGCDetails \
                      -XX:+PrintGCTimeStamps \
                      -XX:+PrintHeapAtGC \
                      -XX:+PrintGCApplicationStoppedTime \
                      -XX:+PrintReferenceGC \
                      -Dcom.sun.management.jmxremote.authenticate=false \
                      -Dcom.sun.management.jmxremote.ssl=false"
        cd ${app_home}/${app_name}
        nohup java  -jar ${app_name}.jar  --spring.profiles.active=$env  ${java_opts}  >/data/opt/logs/start/duanju-api.log 2>&1 &
        echo start application success
}

if [ -z "$PID" ]
then
    echo Application is already stopped
else
    app_stop
fi
sleep 3
app_start $env