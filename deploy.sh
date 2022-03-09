#!/bin/sh

set -x

if [ $# != 2 ]
then
    echo -e "\033[31mPlease enter the arguments...\033[0m"
    echo -e "\033[31mUSAGE:: sh $0 version [update|stop|start|restart]"
    exit 1
fi

deployBin=$0
version=$1
command=$2

dir="$(cd `dirname $deployBin`;pwd)"
appLibDir="${dir}/lib"
projectName="sports-spider"

function update() {
    # git fetch -p
    # git checkout origin/$version

    mvn clean package -U -DskipTests
    if [ $? != 0 ]
    then
        echo -e "\033[31mcompile error...\033[0m"
        exit 1
    fi

    mkdir -p ${appLibDir}
    mv target/${projectName}-*.jar ${appLibDir}/
    nohup java -jar ${appLibDir}/${projectName}*.jar 2>&1 > /dev/null &
}




case $2 in
update)
    update;;
esac
