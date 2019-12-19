# Specify which OS image to run.
# In our case, we are using an OS image with WildFly started
# as a daemon/service
FROM jboss/wildfly:18.0.1.Final

# Copy the generated WAR from "target" folder into the Docker image,
# in the folder where Widlfly is expecting to find installed WAR files
COPY target/examples.war /opt/jboss/wildfly/standalone/deployments/

# No need of CMD here, as WidlFly is automatically started as a service



# To run this example, we first need to build the examples.war file with
#
# mvn package
#
# then, we build a Docker image with:
#
# docker build . -t examples
#
# then, we run such image with
#
# docker run -p 8080:8080 examples
#
# finally, we can access the web app at:
#
# localhost:8080/examples




