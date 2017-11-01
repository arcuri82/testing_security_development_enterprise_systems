FROM openjdk:8

ADD target/sender.jar .

# Note here that we start the Spring application with the "dc" profile

CMD java -jar sender.jar  --spring.profiles.active=dc