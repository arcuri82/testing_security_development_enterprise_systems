package org.tsdes.intro.spring.testing.selenium.jsftests.selenium;

import org.junit.ClassRule;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;

public class SeleniumDockerIT extends SeleniumTestBase {

    private static String HOST_ALIAS = "jsf-tests";

    @ClassRule
    public static Network network = Network.newNetwork();

    @ClassRule
    public static GenericContainer spring = new GenericContainer(
            new ImageFromDockerfile("jsf-tests")
                    .withFileFromPath("target/spring-jsf-exec.jar",
                            Paths.get("../../../jsf/target/spring-jsf-exec.jar"))
                    .withFileFromPath("Dockerfile", Paths.get("../../../jsf/Dockerfile")))
            .withExposedPorts(8080)
            .withNetwork(network)
            .withNetworkAliases(HOST_ALIAS)
            .waitingFor(Wait.forHttp("/"));

    @Rule
    public BrowserWebDriverContainer browser = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
            .withNetwork(network);


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
