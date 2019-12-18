# ONLY used for testing

FROM adoptopenjdk/openjdk11:alpine

COPY target/quizgame-exec.jar .


CMD java -jar quizgame-exec.jar \
    --spring.datasource.username="postgres"  \
    --spring.datasource.password  \
    --spring.datasource.url="jdbc:postgresql://postgresql-host:5432/postgres"





