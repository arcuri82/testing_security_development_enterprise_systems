package org.tsdes.intro.spring.testing.selenium.jsftests.selenium;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;

public class SeleniumDockerIT extends SeleniumTestBase {

    private static String HOST_ALIAS = "jsf-tests";

    public static Network network = Network.newNetwork();

    /*
        Here we first start the Spring Application once (@ClassRule only apply once).
        Then, before each test, we start new container with the browser, as
        as @Rule is applied per test
     */


    public static GenericContainer spring = new GenericContainer(
            new ImageFromDockerfile("jsf-tests")
                    .withFileFromPath("target/spring-jsf-exec.jar",
                            Paths.get("../../../jsf/target/spring-jsf-exec.jar"))
                    .withFileFromPath("Dockerfile", Paths.get("../../../jsf/Dockerfile")))
            .withExposedPorts(8080)
            .withNetwork(network)
            .withNetworkAliases(HOST_ALIAS)
            .waitingFor(Wait.forHttp("/"))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("SPRING-APPLICATION")))
            ;


    public static BrowserWebDriverContainer browser = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
            .withNetwork(network);


    @BeforeAll
    public static void init(){
        spring.start();
        browser.start();
    }

    @AfterAll
    public static void tearDown(){
        browser.stop();
        spring.stop();
    }

    @Override
    protected WebDriver getDriver() {
        return browser.getWebDriver();
    }

    @Override
    protected String getServerHost() {
        return HOST_ALIAS;
    }

    @Override
    protected int getServerPort() {
        return 8080;
    }
}
