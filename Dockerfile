FROM adoptopenjdk/openjdk11:alpine-jre

LABEL name="Myretail" \
      version="1.*"


MAINTAINER madan.kahate@gmail.com

RUN mkdir /opt/project
ADD ./build/libs/myretail-2.0.1.jar  /opt/project/app.jar
ADD ./scripts/runApp.sh  /opt/project/runApp.sh

RUN apk update \
 && apk upgrade --force-overwrite \
 && apk add --no-cache openjdk11-jre nss \
 && chown -R 1000:1000 /opt/project

ENV JAVA_HOME=/usr/lib/jvm/default-jvm/jre

USER 1000
ENTRYPOINT ["sh", "/opt/project/runApp.sh"]
