# Specify which OS image to run.
# In our case, we are using an OS image with WildFly started
# as a daemon/service
FROM jboss/wildfly:14.0.1.Final

# Copy the generated WAR from "target" folder into the Docker image,
# in the folder where Widlfly is expecting to find installed WAR files
ADD target/examples.war /opt/jboss/wildfly/standalone/deployments/

# No need of CMD here, as WidlFly is automatically started as a service