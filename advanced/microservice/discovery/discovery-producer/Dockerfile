FROM openjdk:8-alpine

ADD target/producer.jar .

CMD java -jar producer.jar   --spring.profiles.active=docker