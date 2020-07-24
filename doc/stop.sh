#使用spring-boot-starter-actuator关闭
curl -X POST 127.0.0.1:8085/weiziplus/shutdown
#方法二
# kill -9 $(ps -ef | grep java | grep 8080 | awk '{print $2}')
