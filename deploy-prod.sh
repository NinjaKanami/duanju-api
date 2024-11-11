#!/bin/bash
env=$1
app_name=duanju-api
app_home=/home/www


PID=$(ps -ef | grep ${app_name}.jar | grep -v grep | awk '{ print $2}')

function app_stop()
{
         checkjavapid=`ps -ef | grep ${app_name}.jar | grep -v grep | awk '{ print $2}'`
         if [[ ! $checkjavapid ]];then
           echo -e "\r${app_name}未启动"
           return
         fi
         kill -15 $checkjavapid
         times=30
         for e in $(seq 30)
         do
             sleep 1
             COSTTIME=$(($times - $e ))
             checkjavapid=`ps -ef | grep ${app_name}.jar | grep -v grep | awk '{ print $2}'`
             if [[ $checkjavapid ]];then

                 echo -e  "\r        -- 停止服务剩余 `expr $COSTTIME` 秒."
             else
                 echo -e "\r服务已停止"
                 break
             fi
         done
         if [[ $checkjavapid ]];then
         echo -e "\r服务停止失败! 开始kill进程!"
         kill -9 $checkjavapid
         echo -e "\r服务 ${app_name} 进程 $checkjavapid 已被杀"
         fi

}

function app_start()
{
        java_opts="-Xms1024m -Xmx1024m \
                      -XX:+UseG1GC \
                      -XX:+HeapDumpOnOutOfMemoryError \
                      -XX:HeapDumpPath=dump.hprof \
                      -XX:+PrintGCDetails \
                      -XX:+PrintGCTimeStamps \
                      -XX:+PrintHeapAtGC \
                      -XX:+PrintGCApplicationStoppedTime \
                      -XX:+PrintReferenceGC \
                      -Dcom.sun.management.jmxremote.authenticate=false \
                      -Dcom.sun.management.jmxremote.ssl=false \
                      -Dfastjson.parser.safeMode=true "
        cd ${app_home}/${app_name}
        nohup java  -jar ${app_name}.jar --spring.profiles.active=prod ${java_opts}  >/dev/null 2>&1 &
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