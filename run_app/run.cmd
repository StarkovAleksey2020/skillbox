cd ..
tskill BookStore.jar

@echo build maven project
call mvnw clean -Dmaven.test.skip package
@echo run project
call java -jar target/BookStore.jar