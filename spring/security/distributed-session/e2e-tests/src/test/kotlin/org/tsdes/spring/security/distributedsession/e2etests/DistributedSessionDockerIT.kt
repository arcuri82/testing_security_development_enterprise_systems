package org.tsdes.spring.security.distributedsession.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.contains
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.util.concurrent.TimeUnit


class DistributedSessionDockerIT {

    companion object {

        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)


        @ClassRule
        @JvmField
        val env = KDockerComposeContainer(File("../docker-compose.yml"))
                .withLocalCompose(true)

        private var counter = System.currentTimeMillis()

        @BeforeClass
        @JvmStatic
        fun initialize() {
            RestAssured.baseURI = "http://localhost"
            RestAssured.port = 80
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()


            await().atMost(60, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until({

                        given().get("http://localhost:80/user").then().statusCode(401)

                        given().get("http://localhost:80/user-service/usersInfo")
                                .then().statusCode(401)

                        true
                    })
        }
    }


    @Test
    fun testUnauthorizedAccess() {

        given().get("/user")
                .then()
                .statusCode(401)
    }

    private fun registerUser(id: String, password: String): String? {

        return given().contentType(ContentType.URLENC)
                .formParam("the_user", id)
                .formParam("the_password", password)
                .post("/signIn")
                .then()
                .statusCode(204)
                .extract().cookie("SESSION")
    }

    private fun createUniqueName(): String {
        counter++
        return "foo_$counter"
    }


    @Test
    fun testLogin() {

        val name = createUniqueName()
        val pwd = "bar"

        val cookie = registerUser(name, pwd)

        given().get("/user")
                .then()
                .statusCode(401)

        //note the difference in cookie name
        given().cookie("SESSION", cookie)
                .get("/user")
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("roles", contains("ROLE_USER"))


        given().auth().basic(name, pwd)
                .get("/user")
                .then()
                .statusCode(200)
                .cookie("SESSION")
                .body("name", equalTo(name))
                .body("roles", contains("ROLE_USER"))
    }

    @Test
    fun testOpenCount(){

        val x = given().basePath("/user-service/usersInfoCount")
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString().toInt()

        assertTrue(x >= 0 )
    }



    @Test
    fun testForbiddenToChangeOthers() {

        val firstId = createUniqueName()
        val firstCookie = registerUser(firstId, "123")
        val firstPath = "/user-service/usersInfo/$firstId"

        /*
            In general, it can make sense to have the DTOs in their
            own module, so can be reused in the client directly.
            Otherwise, we would need to craft the JSON manually,
            as done in these tests
         */

        given().cookie("SESSION", firstCookie)
                .get("/user")
                .then()
                .statusCode(200)
                .body("name", equalTo(firstId))
                .body("roles", contains("ROLE_USER"))


        given().cookie("SESSION", firstCookie)
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "userId": "$firstId",
                        "name": "A",
                        "surname": "B",
                        "email": "a@a.com"
                    }
                    """)
                .put(firstPath)
                .then()
                .statusCode(201)


        val secondId = createUniqueName()
        val secondCookie = registerUser(secondId, "123")
        val secondPath = "/user-service/usersInfo/$secondId"

        given().cookie("SESSION", secondCookie)
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "userId": "$secondId",
                        "name": "bla",
                        "surname": "bla",
                        "email": "bla@bla.com"
                    }
                    """)
                .put(secondPath)
                .then()
                .statusCode(201)



        given().cookie("SESSION", firstCookie)
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "userId": "$secondId"
                    }
                    """)
                .put(secondPath)
                .then()
                .statusCode(403)
    }
}