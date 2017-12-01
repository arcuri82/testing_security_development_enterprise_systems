package org.tsdes.jee.exercises.mycantina.frontend;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;

/**
 * Created by arcuri82 on 29-Nov-17.
 */
public class SeleniumDockerIT extends SeleniumTestBase {

    @ClassRule
    public static GenericContainer jee = new GenericContainer(
            new ImageFromDockerfile("my-cantina")
                    .withFileFromPath("target/my-cantina-frontend-0.0.1-SNAPSHOT.war",
                            Paths.get("target/my-cantina-frontend-0.0.1-SNAPSHOT.war"))
                    .withFileFromPath("Dockerfile", Paths.get("Dockerfile")))
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/my_cantina"));

    @Rule
    public BrowserWebDriverContainer browser = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
            .withNetworkMode(jee.getNetworkMode());


    @Override
    protected WebDriver getDriver() {
        return browser.getWebDriver();
    }

    @Override
    protected String getJeeHost() {
        return jee.getContainerInfo()
                .getNetworkSettings()
                .getNetworks().values().iterator().next()
                .getIpAddress();
    }

    @Override
    protected int getJeePort() {
        return 8080;
    }
}
