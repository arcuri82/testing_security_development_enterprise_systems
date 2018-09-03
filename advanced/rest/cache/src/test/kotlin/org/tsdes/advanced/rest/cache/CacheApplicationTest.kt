package org.tsdes.advanced.rest.cache

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by arcuri82 on 30-Aug-18.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CacheApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CacheApplicationTest{

    @LocalServerPort
    protected var port = 0

    /*
        Note: here we are simulating 2 different services: X and Y.
        But, for simplicity, we run them both in the same JVM and
        Spring context.
     */

    @Autowired
    protected lateinit var  yrest: YRest

    @Autowired
    protected lateinit var xRest: XRest

    @Value("#{cacheManager.getCache('httpClient')}")
    private lateinit var httpClientCache: Cache

    @Before
    fun init() {
        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        xRest.port = port
        yrest.howOftenCalled = 0
        httpClientCache.clear()
    }


    @Test
    fun testHowOftenCalled(){

        assertEquals(0, yrest.howOftenCalled)

        given().accept(ContentType.JSON).get("/y").then().statusCode(200)
        given().accept(ContentType.JSON).get("/y").then().statusCode(200)

        assertEquals(2, yrest.howOftenCalled)
    }

    @Test
    fun testIndirectCall(){

        assertEquals(0, yrest.howOftenCalled)

        //calling /x instead of /y
        given().accept(ContentType.JSON).get("/x").then().statusCode(200)

        assertEquals(1, yrest.howOftenCalled)
    }

    @Test
    fun testCache(){

        assertEquals(0, yrest.howOftenCalled)

        //3 calls, likely within 2 seconds, so 2nd and 3rd should had used cache
        given().accept(ContentType.JSON).get("/x").then().statusCode(200)
        given().accept(ContentType.JSON).get("/x").then().statusCode(200)
        given().accept(ContentType.JSON).get("/x").then().statusCode(200)

        assertEquals(1, yrest.howOftenCalled)
    }

    @Test
    fun testExpiredCache(){

        assertEquals(0, yrest.howOftenCalled)

        given().accept(ContentType.JSON).get("/x").then().statusCode(200)

        //wait more than the TTL
        Thread.sleep(3_000)

        //new call, no cache, as 2s expired
        given().accept(ContentType.JSON).get("/x").then().statusCode(200)

        assertEquals(2, yrest.howOftenCalled)
    }

}