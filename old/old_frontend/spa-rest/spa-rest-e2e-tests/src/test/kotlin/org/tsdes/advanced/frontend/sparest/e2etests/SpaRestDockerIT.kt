package org.tsdes.advanced.frontend.sparest.e2etests

import org.junit.Ignore
import org.junit.Rule
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.testcontainers.containers.BrowserWebDriverContainer


/*
   WARNING: withNetworkMode() seems not working properly, as not
   setting up the network with given input name
 */
@Ignore
class SpaRestDockerIT : SpaRestSeleniumTestBase(){

    /*
        To run Selenium tests, we need a browser and drivers for it, which will
        depend on the operating system.
        What about starting it from a Docker?
        Yes! That works great. Only caveat is that it would be on a different network,
        and so Selenium would not be able to interact with the frontend.
        Adding browser in the docker-compose.yml would be wrong, as we do not need it in
        production.
        So, the approach here is to start it in its own Docker, and then make sure
        such Docker image share the same network of the Docker Compose one.
    */


    class KBrowserWebDriverContainer : BrowserWebDriverContainer<KBrowserWebDriverContainer>()

    @Rule
    @JvmField
    val browser: KBrowserWebDriverContainer = KBrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withNetworkMode("spa-rest-network")

    override fun getDriver(): WebDriver {
        return browser.webDriver
    }

    override fun getServerHost(): String {
        return "frontend" // same name as in docker-compose.yml file
    }

    override fun getServerPort(): Int {
        return 8080
    }

}