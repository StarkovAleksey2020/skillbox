#!/bin/bash
cd ..
pkill -f BookStore.jar

printf build maven project
mvn clean -DskipTests install -Drat.skip=true
printf run project

java -jar target/BookStore.jar &
