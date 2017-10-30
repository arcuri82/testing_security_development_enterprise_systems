package org.tsdes.spring.microservice.gateway.e2etests

import io.restassured.RestAssured.given
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.openqa.selenium.remote.DesiredCapabilities
import org.testcontainers.containers.BrowserWebDriverContainer
import java.io.File


class GatewayIntegrationDockerRestIT {


    private fun assertWithinTime(timeoutMS: Long, lambda: () -> Any) {
        val start = System.currentTimeMillis()

        var delta = 0L

        while (delta < timeoutMS) {
            delta = try {
                lambda.invoke()
                return
            } catch (e: AssertionError) {
                Thread.sleep(100)
                System.currentTimeMillis() - start
            } catch (e: Exception) {
                Thread.sleep(100)
                System.currentTimeMillis() - start
            }
        }

        lambda.invoke()
    }


    class KBrowserWebDriverContainer : BrowserWebDriverContainer<KBrowserWebDriverContainer>()

    @Rule
    @JvmField
    var browser = KBrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withNetworkMode("gateway_default")
            /*
                TODO: note following is not working with joined network.
                But should be doable once can start Compose from test and expose
                Zuul service:
                https://github.com/testcontainers/testcontainers-java/issues/282
             */
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, File("./target/"))



    /*
        TODO Docker-Compose
        docker-compose build
        docker-compose up
     */


    @Ignore
    @Test
    fun testIntegration() {

        assertWithinTime(180_000, {
            given().get("http://localhost:80/index.html").then().statusCode(200)

            given().accept("application/json;charset=UTF-8")
                    .get("http://localhost:80/service/messages")
                    .then().statusCode(200)
        })

        val po = IndexPageObject(browser.webDriver)

        /*
            Cannot directly connect to localhost:80, as
            testHostIpAddress  does not work on Mac. See:

            https://github.com/testcontainers/testcontainers-java/issues/166

            Need to make the Selenium container join the network of Compose
         */

        po.goToPage("zuul", 8080)
        assertTrue(po.isOnPage())

        po.deleteMessages()
        assertEquals(0, po.numberOfMessages())

        po.sendMessage("Hail Zuul!!!")
        assertEquals(1, po.numberOfMessages())

        po.sendMessage("Just kidding")
        assertEquals(2, po.numberOfMessages())

        po.deleteMessages()
        assertEquals(0, po.numberOfMessages())
    }
}