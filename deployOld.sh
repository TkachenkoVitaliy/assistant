#! /bin/bash
echo 'start deploy'
echo 'remove docker container'
docker rm --force back
echo 'remove docker image'
docker rmi --force backend
echo 'build docker image'
docker build -t backend .
echo 'run docker container'
docker run -d -it -p 80:80 --restart always --name back backend
echo 'add to network'
docker network inspect dockernet >/dev/null 2>&1 || docker network create --driver bridge dockernet
docker network connect dockernet back
echo 'deploy completed'