FROM adoptopenjdk/openjdk11:alpine

COPY target/producer.jar .

CMD java -jar producer.jar   --spring.profiles.active=docker