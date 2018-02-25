package org.tsdes.intro.exercises.quizgame.selenium;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.tsdes.intro.exercises.quizgame.Application;

import java.nio.file.Paths;

@ContextConfiguration(initializers = SeleniumDockerIT.DockerInitializer.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SeleniumDockerIT extends SeleniumTestBase {

    private static String QUIZ_HOST_ALIAS = "quizgame-host";
    private static String PG_ALIAS = "postgresql-host";

    @ClassRule
    public static Network network = Network.newNetwork();


    @ClassRule
    public static GenericContainer postgres = new GenericContainer("postgres:10")
            .withExposedPorts(5432)
            .withNetwork(network)
            .withNetworkAliases(PG_ALIAS)
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("POSTGRES")))
            ;


    public static class DockerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            //get the host:port on local host to connect to Postgres inside Docker
            String host = postgres.getContainerIpAddress();
            int port = postgres.getMappedPort(5432);

            EnvironmentTestUtils.addEnvironment(
                    "testcontainers",
                    configurableApplicationContext.getEnvironment(),
                    "spring.datasource.url=jdbc:postgresql://" + host + ":" + port + "/postgres",
                    "spring.datasource.username=postgres",
                    "spring.datasource.password"
            );
        }
    }


    public static GenericContainer spring = new GenericContainer(
            new ImageFromDockerfile("quizgame")
                    .withFileFromPath("target/quizgame-exec.jar",
                            Paths.get("target/quizgame-exec.jar"))
                    .withFileFromPath("Dockerfile", Paths.get("Dockerfile")))
            .withExposedPorts(8080)
            .withNetwork(network)
            .withNetworkAliases(QUIZ_HOST_ALIAS)
            .waitingFor(Wait.forHttp("/"))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("QUIZGAME")))
            ;

    public BrowserWebDriverContainer browser = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
            .withNetwork(network);

    @Rule
    public RuleChain chain = RuleChain.outerRule(spring).around(browser);


    @Override
    protected WebDriver getDriver() {
        return browser.getWebDriver();
    }

    @Override
    protected String getServerHost() {
        return QUIZ_HOST_ALIAS;
    }

    @Override
    protected int getServerPort() {
        return 8080;
    }
}
