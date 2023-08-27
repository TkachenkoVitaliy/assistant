#! /bin/bash
git stash
mvn -s settings.xml clean install -DskipTests
mv target/assistant-*.jar target/assistant.jar
mv JenkinsDockerfile target/JenkinsDockerfile
docker rm --force back
docker rmi --force backend
cd target && docker build -t backend -f JenkinsDockerfile .
docker run -d -it -p 8089:8089 --restart always --name back backend
docker network inspect dockernet >/dev/null 2>&1 || docker network create --driver bridge dockernet
docker network connect dockernet back