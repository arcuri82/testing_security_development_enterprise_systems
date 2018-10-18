package org.tsdes.advanced.microservice.gateway.e2etests

import io.restassured.RestAssured.given
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.openqa.selenium.remote.DesiredCapabilities
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.util.concurrent.TimeUnit

@Ignore //FIXME
class GatewayIntegrationDockerRestIT {

    companion object {

        @BeforeClass
        @JvmStatic
        fun checkEnvironment(){

            /*
                TODO
                Looks like currently some issues in running Docker-Compose on Travis
             */

            val travis = System.getProperty("TRAVIS") != null
            Assume.assumeTrue(!travis)
        }

        class KDockerComposeContainer(id: String, path: File) : DockerComposeContainer<KDockerComposeContainer>(id, path)

        val composeId = "gateway"

        @ClassRule
        @JvmField
        val env = KDockerComposeContainer(composeId, File("../docker-compose.yml"))
                /*
                    In the docker-compose file, I did not expose the port for Eureka.
                    Nor I made a route for it in Zuul.
                    However, I need it in these tests, as need to query it to wait for it
                    be fully initialized.
                    So, I expose it here.
                 */
                .withExposedService("eureka", 8761)
                .withLocalCompose(true)
    }

    /*
        To run Selenium tests, I need a browser and drivers for it, which will
        depend on the operating system.
        What about starting it from a Docker?
        Yes! That works great. Only caveat is that it would be on a different network,
        and so Selenium would not be able to interact with Zuul.
        Adding it in the docker-compose.yml would be wrong, as we do not need it in
        production.
        So, the approach here is to start it in its own Docker, and then make sure
        such Docker image share the same network of the Docker Compose one.

        FIXME: same issue as in Discovery example. Need to wait for fix in TestContainer
     */

    @Rule
    @JvmField
    var browser = KBrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withNetworkMode("${composeId}_default")
    /*
        TODO: note following would be great feature, but seems currently not working with joined network.
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

            Note: here I am using the Awaitility library to do such waits
         */

        await().atMost(180, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until({

                    given().get("http://localhost:80/index.html").then().statusCode(200)

                    given().accept("application/json;charset=UTF-8")
                            .get("http://localhost:80/service/messages")
                            .then().statusCode(200)

                    given().accept("application/json;charset=UTF-8")
                            .delete("http://localhost:80/service/messages")
                            .then().statusCode(204)

                    given().baseUri("http://${env.getServiceHost("eureka_1", 8761)}")
                            .port(env.getServicePort("eureka_1", 8761))
                            .get("/eureka/apps")
                            .then()
                            .body("applications.application.instance.size()", equalTo(4))

                    true
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


        await().atMost(120, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until({
                    (0..3).forEach { po.sendMessage("foo") }

                    val messages = po.messages()

                    assertEquals(3, messages.toSet().size)

                    assertTrue(messages.any { it.startsWith("A :") })
                    assertTrue(messages.any { it.startsWith("B :") })
                    assertTrue(messages.any { it.startsWith("C :") })

                    true
                })

        po.deleteMessages()
    }
}