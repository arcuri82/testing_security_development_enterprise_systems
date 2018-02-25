# ONLY used for testing

FROM openjdk:8

ADD target/quizgame.jar .


CMD java -jar quizgame.jar --spring.datasource.username="postgres"   --spring.datasource.password  --spring.datasource.url="jdbc:postgresql://postgresql-host:5432/postgres"




