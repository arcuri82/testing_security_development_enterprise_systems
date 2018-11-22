package org.tsdes.advanced.security.distributedsession.userservice

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.security.distributedsession.userservice.dto.UserInfoDto


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest{

    @LocalServerPort
    private var port = 0

    @Autowired
    private lateinit var userInfoRepository : UserInfoRepository


    @BeforeEach
    fun initialize() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.basePath = "/usersInfo"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        userInfoRepository.deleteAll()
    }


    @Test
    fun testNeedAdmin(){

        given().get()
                .then()
                .statusCode(401)
    }


    @Test
    fun testOpenCount(){

        val count = given().basePath("/usersInfoCount")
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString().toInt()

        assertEquals(0, count)
    }



    @Test
    fun testGetAllWithAdmin(){

        checkSize(0)
    }


    private fun checkSize(n: Int){
        given().auth().basic("admin","admin")
                .accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(n))
    }


    @Test
    fun testCreate(){

        checkSize(0)

        val id = "foo"

        val dto = UserInfoDto(id, "A", "B", "C", "a@a.com")

        given().auth().basic(id,"123")
                .contentType(ContentType.JSON)
                .body(dto)
                .put("/$id")
                .then()
                .statusCode(201)

        checkSize(1)
    }


    @Test
    fun testChangeField(){

        checkSize(0)

        val id = "foo"
        val name = "John"

        val dto = UserInfoDto(id, name, "B", "C", "a@a.com")

        given().auth().basic(id,"123")
                .contentType(ContentType.JSON)
                .body(dto)
                .put("/$id")
                .then()
                .statusCode(201)

        val changed = name + "_foo"

        dto.name = changed

        given().auth().basic(id,"123")
                .contentType(ContentType.JSON)
                .body(dto)
                .put("/$id")
                .then()
                .statusCode(204)

        checkSize(1)

        given().auth().basic(id,"123")
                .accept(ContentType.JSON)
                .get("/$id")
                .then()
                .statusCode(200)
                .body("name", equalTo(changed))
    }



    @Test
    fun testForbiddenToChangeOthers(){

        checkSize(0)

        val first = "foo"

        given().auth().basic(first,"123")
                .contentType(ContentType.JSON)
                .body(UserInfoDto(first, "A", "B", "C", "a@a.com"))
                .put("/$first")
                .then()
                .statusCode(201)

        checkSize(1)


        val second = "bar"

        given().auth().basic(second,"123")
                .contentType(ContentType.JSON)
                .body(UserInfoDto(second, "bla", "bla", "bla", "bla@bla.com"))
                .put("/$second")
                .then()
                .statusCode(201)

        checkSize(2)


        given().auth().basic(first,"123")
                .contentType(ContentType.JSON)
                .body(UserInfoDto(second, "forbidden", "forbidden", "forbidden", "a@a.com"))
                .put("/$second")
                .then()
                .statusCode(403)

        checkSize(2)
    }
}