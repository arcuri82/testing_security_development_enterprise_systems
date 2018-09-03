package org.tsdes.advanced.rest.circuitbreaker

import com.netflix.hystrix.Hystrix
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.TimeUnit


/**
 * Created by arcuri82 on 04-Aug-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YRestTest {

    @LocalServerPort
    private var port = 0

    @Autowired
    private lateinit var yRest: YRest

    @Before
    fun reset() {

        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/y"
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

        val res = given().queryParam("v", value)
                .accept(ContentType.ANY)
                .get("/single")
                .then()
                .statusCode(200)
                .extract().asString()

        return res.toLong()
    }

    private fun exe(a: Long, b: Long, c: Long, d: Long, e: Long): Long {

        val res = given()
                .queryParam("a", a)
                .queryParam("b", b)
                .queryParam("c", c)
                .queryParam("d", d)
                .queryParam("e", e)
                .accept(ContentType.ANY)
                .get("/multi")
                .then()
                .statusCode(200)
                .extract().asString()

        return res.toLong()
    }

    @Test
    fun testBase() {

        val value: Long = 50
        val res = exe(value)

        assertEquals(value * 2, res)
    }

    @Test
    fun testTimeout() {

        val value: Long = 600
        val res = exe(value)

        assertEquals(0, res)
    }


    @Test
    fun testCircuitBreaker() {

        val value: Long = 600

        var start = System.currentTimeMillis()
        exe(value)
        exe(value) // this will trigger the circuit breaker
        var delta = System.currentTimeMillis() - start

        assertTrue(delta > 1000) // the 2 calls took at least 1200ms

        start = System.currentTimeMillis()
        // these will all fail immediately, as CB is on.
        // if these were not failing immediately, would take at least 10*600 = 6s
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

        assertTrue("Delta: $delta", delta < 1000) //likely even shorter than 1s
    }


    @Test
    fun testAsync() {

        val value: Long = 300

        //warm up
        exe(value, value, value, value, value)

        val start = System.currentTimeMillis()
        val res = exe(value, value, value, value, value)
        val delta = System.currentTimeMillis() - start

        assertEquals(5 * value * 2, res)
        assertTrue("Delta: $delta", delta < 600)
    }
}