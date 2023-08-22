#
# Build stage
#
FROM maven:3.9.3-amazoncorretto-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mkdir -p /root/.m2 \
    && mkdir /root/.m2/repository
COPY settings.xml /root/.m2
RUN mvn -f /home/app/pom.xml -s /root/.m2/settings.xml clean package -Dmaven.test.skip
RUN #mvn -f /home/app/pom.xml clean package -Dmaven.test.skip

#
# Package stage
#
FROM amazoncorretto:17.0.7-alpine
COPY --from=build /home/app/target/assistant-0.0.1.jar /usr/local/lib/assistant-0.0.1.jar
#RUN apt-get update; apt-get install -y fontconfig libfreetype6
EXPOSE 8089
ENTRYPOINT ["java","-jar","/usr/local/lib/assistant-0.0.1.jar"]