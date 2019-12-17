FROM adoptopenjdk/openjdk11:alpine

COPY target/consumer.jar .

CMD java -jar consumer.jar  --spring.profiles.active=docker