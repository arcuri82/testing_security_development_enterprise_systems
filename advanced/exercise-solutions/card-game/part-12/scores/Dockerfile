FROM adoptopenjdk/openjdk11:alpine

COPY target/scores.jar .

CMD java -Dspring.profiles.active=FakeData  -jar scores.jar