package org.tsdes.advanced.microservice.discovery.producer

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Created by arcuri82 on 15-Oct-18.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProducerApplicationTest {

    @LocalServerPort
    private var port = 0

    @Test
    fun testGetId() {

        val id = given().accept(ContentType.TEXT)
                .get("http://localhost:$port/producerData")
                .then()
                .statusCode(200)
                .extract().body().asString()

        assertEquals("Undefined", id)
    }
}