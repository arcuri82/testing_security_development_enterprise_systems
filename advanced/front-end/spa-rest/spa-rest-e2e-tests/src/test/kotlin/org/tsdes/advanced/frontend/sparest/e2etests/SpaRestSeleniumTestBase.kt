package org.tsdes.advanced.frontend.sparest.e2etests

import io.restassured.RestAssured
import org.awaitility.Awaitility
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.testcontainers.containers.DockerComposeContainer
import org.tsdes.advanced.frontend.sparest.e2etests.po.HomePO
import java.io.File
import java.util.concurrent.TimeUnit

abstract class SpaRestSeleniumTestBase {

    protected abstract fun getDriver(): WebDriver

    protected abstract fun getServerHost(): String

    protected abstract fun getServerPort(): Int


    companion object {


        class KDockerComposeContainer(id: String, path: File) : DockerComposeContainer<KDockerComposeContainer>(id, path)

        private const val composeId = "spa-rest"

        @ClassRule
        @JvmField
        val env: KDockerComposeContainer =
                KDockerComposeContainer(composeId, File("../docker-compose.yml"))
                        .withLocalCompose(true)



        @BeforeClass
        @JvmStatic
        fun waitForServer() {

            /*
                Need to wait for a bit, because servers starting inside Docker might
                take some seconds before they are initialized and can respond to HTTP requests.
                Note: here I am using the Awaitility library to do such waits
             */

            Awaitility.await().atMost(40, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until {
                        RestAssured.given().get("http://localhost:8080/index.html").then().statusCode(200)
                        RestAssured.given().get("http://localhost:8081/books").then().statusCode(200)
                        true
                    }
        }
    }


    private var home: HomePO? = null

    @Before
    fun initTest() {
        home = HomePO(getDriver(), getServerHost(), getServerPort())

        home!!.toStartingPage()

        assertTrue("Failed to start from Home Page", home!!.isOnPage)
    }



    @Test
    fun testHomePage() {

        val displayed = home!!.waitForVisibility(3, By.id("home_create_btn"))

        assertTrue(displayed)
    }

    /*
        Here we could have other tests checking the main features of the application.
        However, starting Docker Compose is quite expensive, and it is more difficult
        to reset state of the app (e.g., data in databases).
        Restarting Docker Compose at each test execution is simply not an option.
     */

}