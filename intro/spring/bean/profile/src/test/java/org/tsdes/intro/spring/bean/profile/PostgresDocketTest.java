package org.tsdes.intro.spring.bean.profile;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.tsdes.intro.spring.bean.jpa.Application;

/**
 * Here, I want to run the same tests, but on a real database.
 * However, I cannot use the same settings as in production code, as
 * those would rely to a production DB.
 * So here I just start a Postgres with Docker using TestContainer.
 * However, I need to change the settings to connect to such database,
 * and I do so by using and activating a specific profile with its
 * own configuration file.
 *
 * Created by arcuri82 on 26-Jan-18.
 */
@ActiveProfiles("docker") //activate profile, load configs from application-docker.yml
@ContextConfiguration(initializers = PostgresDocketTest.DockerInitializer.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class PostgresDocketTest extends DbTestBase {

    /*
        Following rule would start Postgres inside Docker, and expose the
        port 5432 on which Postgres is listening for TCP connections.

        Note: in testing, to avoid port conflicts, we need to use ephemeral ports.
        When TestContainers open a port in Docker, it will map it to an ephemeral
        one on the host OS.
     */
    public static GenericContainer postgres = new GenericContainer("postgres:10")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_HOST_AUTH_METHOD","trust");


    @BeforeAll
    public static void init(){
        postgres.start();
    }

    @AfterAll
    public static void tearDown(){
        postgres.stop();
    }

    /*
        As actual host/port will be known only at runtime (eg ephemeral port), we cannot set
        the spring.datasource.url in the application.yml file.
        It has to be set at runtime.
        We use @ContextConfiguration to do that
     */
    public static class DockerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            //get the host:port on local host to connect to Postgres inside Docker
            String host = postgres.getContainerIpAddress();
            int port = postgres.getMappedPort(5432);

            TestPropertyValues.of("spring.datasource.url=jdbc:postgresql://" + host + ":" + port + "/postgres")
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    @Override
    protected String getExpectedDatabaseName() {
        return "postgresql";
    }
}
