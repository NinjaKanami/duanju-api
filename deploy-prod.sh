#!/bin/bash
action=$1
app_name=duanju
app_home=/home/ubuntu/duanju-api


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
        java_opts="-Xms512m -Xmx512m \
                      -Djava.security.egd=/dev/urandom "
        cd ${app_home}/
        nohup java -javaagent:${app_home}/opentelemetry-javaagent.jar -Dotel.resource.attributes=service.name=duanju,token=vchZEkGkDVEJRKQjIYre -Dotel.exporter.otlp.endpoint=http://pl.ap-shanghai.apm.tencentcs.com:4317 -jar ${app_name}.jar --spring.profiles.active=prod  > start.log 2>&1 &
        echo start application success
}

if [ -z "$PID" ]
then
    echo Application is already stopped
else
    app_stop
fi

if [ "x${action}" != "xstop" ];then
  sleep 3
  app_start $env
fi

