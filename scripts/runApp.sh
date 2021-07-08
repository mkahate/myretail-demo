#!/usr/bin/env bash
set -e

/usr/bin/java -XX:+UseConcMarkSweepGC -XX:+UnlockExperimentalVMOptions -XX:MaxRAMFraction=1 -XX:+CMSParallelRemarkEnabled \
-XX:+UseCMSInitiatingOccupancyOnly -XshowSettings:vm \
-Djava.security.egd=file:/dev/./urandom -jar /opt/project/app.jar
