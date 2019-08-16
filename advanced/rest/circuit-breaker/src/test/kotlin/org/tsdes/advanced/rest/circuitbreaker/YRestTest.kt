package org.tsdes.advanced.rest.circuitbreaker

import com.netflix.hystrix.Hystrix
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.concurrent.TimeUnit


/**
 * Created by arcuri82 on 04-Aug-17.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YRestTest {

    @LocalServerPort
    private var port = 0

    @Autowired
    private lateinit var yRest: YRest

    @BeforeEach
    fun reset() {

        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        /*
            NOTE: this works ONLY because we are running the RESTful service in the
            same JVM of the tests
         */
        Hystrix.reset(10, TimeUnit.SECONDS)

        /*
            Looks like a bug in Hystrix, whose "reset" method does not really
            seem to guarantee to be blocking until EVERYTHING is actually reset.
            So here we do an explicit extra wait.
         */
        Thread.sleep(5_000)

        //make sure Y is calling X on the right port
        yRest.port = port
    }


    private fun exe(value: Long): Long {

        return given().queryParam("v", value)
                .accept(ContentType.ANY)
                .get("/y")
                .then()
                .statusCode(200)
                .extract().asString().toLong()
    }


    @Test
    fun testBase() {

        val value: Long = 50
        val res = exe(value)

        assertEquals(value, res)
    }

    @Test
    fun testTimeout() {

        val value: Long = 600
        val res = exe(value)

        assertEquals(0, res) // 600 > 500
    }


    @Test
    fun testCircuitBreaker() {

        val value: Long = 600

        var start = System.currentTimeMillis()
        exe(value)
        exe(value) // this will trigger the circuit breaker, as 2 failures due to timeout 600 > 500
        var delta = System.currentTimeMillis() - start

        assertTrue(delta >= 1000) // the 2 calls took at least 1000ms

        start = System.currentTimeMillis()
        // these will all fail immediately, as CB is on.
        // if these were not failing immediately, would take at least 10*500 = 5s
        exe(value)
        exe(value)
        exe(value)
        exe(value)
        exe(value)
        exe(value)
        exe(value)
        exe(value)
        exe(value)
        exe(value)
        delta = System.currentTimeMillis() - start

        assertTrue(delta < 1000, "Delta: $delta") //likely way shorter than 1s
    }

}