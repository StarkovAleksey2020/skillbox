<h1 align="center">Book Store</h1>
<h4 align="center">Technology: Spring Boot, JavaScript, HTML, CSS, PostgreSQL, Liquibase</h4>
<h3 align="center">Alexey Starkov educational project<br/>
SkillBox Java-Framework-Spring Course</h3>

## Project setup&run
#### Linux
```
#!/bin/bash
pkill -f BookStore.jar
printf build maven project
mvn clean -DskipTests install -Drat.skip=true
printf run project
java -jar target/BookStore.jar &
```
#### /run_app/run.sh - application launch script for Linux

#### Windows
```
tskill BookStore.jar
@echo build maven project
call mvnw clean -Dmaven.test.skip package
@echo run project
call java -jar target/BookStore.jar
```
#### /run_app/run.cmd - application launch script for Windows

## Reference materials

#### /notes/db_recreate.txt - procedure for rebuilding the database for Linux
