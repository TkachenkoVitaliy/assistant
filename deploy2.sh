#! /bin/bash
echo 'start mvn install'
mvn -s settings.xml clean install
mv target/assistant-*.jar target/assistant.jar
mv JenkinsDockerfile target/JenkinsDockerfile
echo 'remove old container'
docker rm --force back
echo 'remove old image'
docker rmi --force backend
echo 'build new image'
cd target && docker build -t backend -f JenkinsDockerfile .
echo 'run new container'
docker run -d -it -p 8089:8089 --restart always --name back backend
echo 'connect container to dockernet network'
docker network inspect dockernet >/dev/null 2>&1 || docker network create --driver bridge dockernet
docker network connect dockernet back
echo 'complete!!!'