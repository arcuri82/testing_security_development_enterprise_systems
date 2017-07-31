package org.tsdes.spring.rest.patch

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.spring.utils.HttpUtil

/**
 * Created by arcuri82 on 31-Jul-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CounterRestTest {

    @LocalServerPort
    private var port = 0

    @Before
    fun initialize() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/patch/api/counters"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }


    @Test
    fun testCreate() {

        val charset = "UTF-8"

        val name = "Ø counter"
        val value = 5

        val body = "{ \"value\":$value, \"name\":\"$name\"}"
        val data = body.toByteArray(charset(charset))
        val size = data.size

        /*
            note: Ø will take two bytes in UTF-8 format.
            The "Content-Length" is expressed in octets, ie bytes,
            not the length of the Java string.
         */
        assertEquals((body.length + 1).toLong(), size.toLong())

        val basePath = "/patch/api/counters"

        var message = "POST $basePath HTTP/1.1\r\n"
        message += "Host:localhost:$port\r\n"
        message += "Content-Type:application/json; charset=$charset\r\n"
        message += "Content-Length:$size\r\n"
        message += "\r\n"
        message += body

        val response = HttpUtil.executeHttpCommand("localhost", port, message)
        val headers = HttpUtil.getHeaderBlock(response)

        assertTrue(headers, headers.contains("201"))

        //Response should contain header telling where the newly created
        //resource is available
        val location = HttpUtil.getHeaderValue("Location", headers)
        assertTrue(location, location!!.contains(basePath))

        //extract the last path element, ie the {id} in /patch/api/counters/{id}
        val locationId = Integer.parseInt(
                location.substring(
                        location.lastIndexOf('/') + 1,
                        location.length))

        //to check the GET, now just using Rest-Assured
        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("value", equalTo(value))
                .body("name", equalTo(name))
                .body("id", equalTo(locationId))
    }

    private fun createNew(dto: CounterDto) {

        given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
    }

    private operator fun get(id: Long): CounterDto {

        return given().accept(ContentType.JSON)
                .get("$id")
                .then()
                .statusCode(200)
                .extract()
                .`as`(CounterDto::class.java)
    }

    private fun update(dto: CounterDto) {

        given().contentType(ContentType.JSON)
                .body(dto)
                .put("${dto.id}")
                .then()
                .statusCode(204)
    }

    private fun patchWithMergeJSon(id: Long, jsonBody: String, statusCode: Int) {
        given().contentType("application/merge-patch+json")
                .body(jsonBody)
                .patch("$id")
                .then()
                .statusCode(statusCode)
    }

    private fun verifyEqual(a: CounterDto, b: CounterDto) {
        assertEquals(a.id, b.id)
        assertEquals(a.name, b.name)
        assertEquals(a.value, b.value)
    }


    @Test
    fun testPut() {

        val dto = CounterDto(System.currentTimeMillis(), "foo", 5)
        createNew(dto)

        var readBack = get(dto.id!!)
        verifyEqual(dto, readBack)

        val updated = CounterDto(dto.id, null, 0)
        update(updated)
        readBack = get(dto.id!!)

        //whole replacement
        verifyEqual(updated, readBack)
    }

    @Test
    fun testPatchCustomDelta() {

        val value = 46

        val dto = CounterDto(System.currentTimeMillis(), "foo", value)
        createNew(dto)

        val delta = -4

        given().contentType(ContentType.TEXT)
                .body(delta)
                .patch("${dto.id}")
                .then()
                .statusCode(204)

        val readBack = get(dto.id!!)
        assertEquals(value + delta, readBack.value)
    }


    @Test
    fun testPatchMergeJSon() {

        val id = System.currentTimeMillis()
        val value = 55
        val name = "foo"

        val dto = CounterDto(id, name, value)
        createNew(dto)

        val modifiedValue = value * 3

        //delete the name, and change the value
        patchWithMergeJSon(id, "{\"name\":null, \"value\":$modifiedValue}", 204)

        val readBack = get(dto.id!!)
        assertEquals(modifiedValue, readBack.value)
        assertNull(readBack.name)
        assertEquals(id, readBack.id) // should had stayed the same
    }

    @Test
    fun testPatchMergeJSonJustValue() {

        val id = System.currentTimeMillis()
        val value = 55
        val name = "foo"

        val dto = CounterDto(id, name, value)
        createNew(dto)

        val modifiedValue = value * 3

        //just change the value
        patchWithMergeJSon(id, "{\"value\":$modifiedValue}", 204)

        val readBack = get(dto.id!!)
        assertEquals(modifiedValue, readBack.value)
        assertEquals(name, readBack.name) //not modified
        assertEquals(id, readBack.id) // should had stayed the same
    }

    @Test
    fun testPatchMergeJSonInvalidValue() {

        val id = System.currentTimeMillis()
        val value = 55
        val name = "foo"

        val dto = CounterDto(id, name, value)
        createNew(dto)

        val modifiedValue = value * 3
        val modifiedName = "modified from " + name

        //name is correct, but value is wrongly passed as string, not integer
        patchWithMergeJSon(id, "{\"name\":\"$modifiedName\", \"value\":\"$modifiedValue\"}", 400)

        val readBack = get(dto.id!!)
        //nothing should had been modified, as value was invalid
        assertEquals(value, readBack.value)
        assertEquals(name, readBack.name) //IMPORTANT that this did not change
        assertEquals(id, readBack.id)
    }
}