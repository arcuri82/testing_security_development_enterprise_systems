package org.tsdes.spring.microservice.gateway.e2etests

import io.restassured.RestAssured.given
import org.awaitility.Awaitility.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.openqa.selenium.remote.DesiredCapabilities
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.util.concurrent.TimeUnit


class GatewayIntegrationDockerRestIT {

    companion object {

        class KDockerComposeContainer(id: String, path: File) : DockerComposeContainer<KDockerComposeContainer>(id, path)

        val composeId = "gateway"

        @ClassRule
        @JvmField
        val env = KDockerComposeContainer(composeId, File("../docker-compose.yml"))
                .withLocalCompose(true)
    }

    @Rule
    @JvmField
    var browser = KBrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withNetworkMode("${composeId}_default")
    /*
        TODO: note following seems currently not working with joined network.
        https://github.com/testcontainers/testcontainers-java/issues/282
     */
//            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, File("./target/"))

    class KBrowserWebDriverContainer : BrowserWebDriverContainer<KBrowserWebDriverContainer>()


    @Before
    fun waitForServer() {

        /*
            Need to wait for a bit, because it can take up to 2 minutes before
            Eureka is fully initialized and all changes are propagated to all
            of its clients. See:
            https://github.com/Netflix/eureka/wiki/Understanding-eureka-client-server-communication

            Note: here I am using the Awaitility library
         */

        await().atMost(180, TimeUnit.SECONDS).until({
            try {
                given().get("http://localhost:80/index.html").then().statusCode(200)

                given().accept("application/json;charset=UTF-8")
                        .get("http://localhost:80/service/messages")
                        .then().statusCode(200)

                given().accept("application/json;charset=UTF-8")
                        .delete("http://localhost:80/service/messages")
                        .then().statusCode(204)

                true

            } catch (e: AssertionError) {
                false
            } catch (e: Exception) {
                false
            }
        })
    }

    @Test
    fun testIntegration() {

        val po = IndexPageObject(browser.webDriver)

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

    @Test
    fun testLoadBalance() {

        val po = IndexPageObject(browser.webDriver)

        po.goToPage("zuul", 8080)
        assertTrue(po.isOnPage())

        po.deleteMessages()
        assertEquals(0, po.numberOfMessages())

        (0..20).forEach { po.sendMessage("foo", false) }

        val messages = po.messages()

        assertTrue(messages.any { it.startsWith("A :") })
        assertTrue(messages.any { it.startsWith("B :") })
        assertTrue(messages.any { it.startsWith("C :") })

        po.deleteMessages()
    }
}