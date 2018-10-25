package org.tsdes.spring.security.distributedsession.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.contains
import org.junit.*
import org.junit.Assert.assertTrue
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.util.concurrent.TimeUnit

@Ignore //FIXME
class DistributedSessionDockerIT {

    companion object {

        @BeforeClass @JvmStatic
        fun checkEnvironment(){

            /*
                TODO
                Looks like currently some issues in running Docker-Compose on Travis
             */

            val travis = System.getProperty("TRAVIS") != null
            Assume.assumeTrue(!travis)
        }

        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)


        @ClassRule
        @JvmField
        val env = KDockerComposeContainer(File("../docker-compose.yml"))
                .withLocalCompose(true)
                //if needed for debugging
                .withLogConsumer("user-service") {System.out.println("[DOCKER] " + it.utf8String)}

        private var counter = System.currentTimeMillis()

        @BeforeClass
        @JvmStatic
        fun initialize() {
            RestAssured.baseURI = "http://localhost"
            RestAssured.port = 80
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()


            await().atMost(90, TimeUnit.SECONDS)
                    .pollInterval(6, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until {

                        given().get("http://localhost:80/auth/user").then().statusCode(401)

                        given().get("http://localhost:80/user-service/usersInfo")
                                .then().statusCode(401)

                        true
                    }
        }
    }


    @Test
    fun testUnauthorizedAccess() {

        given().get("/auth/user")
                .then()
                .statusCode(401)
    }


    private fun registerUser(id: String, password: String): String {

        val sessionCookie = given().contentType(ContentType.JSON)
                .body("""
                    {"userId": "$id", "password": "$password"}
                """.trimIndent())
                .post("/auth/signUp")
                .then()
                .statusCode(204)
                .header("Set-Cookie", not(equalTo(null)))
                .cookie("SESSION")
                .extract().cookie("SESSION")

        return sessionCookie
    }

    private fun createUniqueId(): String {
        counter++
        return "foo_$counter"
    }


    @Test
    fun testLogin() {

        val id = createUniqueId()
        val pwd = "bar"

        val cookie = registerUser(id, pwd)

        given().get("/auth/user")
                .then()
                .statusCode(401)

        given().cookie("SESSION", cookie)
                .get("/auth/user")
                .then()
                .statusCode(200)
                .body("name", equalTo(id))
                .body("roles", contains("ROLE_USER"))


        given().auth().basic(id, pwd)
                .get("/auth/user")
                .then()
                .statusCode(200)
                .cookie("SESSION")
                .body("name", equalTo(id))
                .body("roles", contains("ROLE_USER"))

        given().contentType(ContentType.JSON)
                .body("""
                    {"userId": "$id", "password": "$pwd"}
                """.trimIndent())
                .post("/auth/login")
                .then()
                .statusCode(204)
                .cookie("SESSION")
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

        val firstId = createUniqueId()
        val firstCookie = registerUser(firstId, "123")
        val firstPath = "/user-service/usersInfo/$firstId"

        /*
            In general, it can make sense to have the DTOs in their
            own module, so can be reused in the client directly.
            Otherwise, we would need to craft the JSON manually,
            as done in these tests
         */

        given().cookie("SESSION", firstCookie)
                .get("/auth/user")
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


        val secondId = createUniqueId()
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


    @Test
    fun testGetGreetings(){

        val id = createUniqueId()
        val name = "foo"
        val pwd = "bar"

        val cookie = registerUser(id, pwd)


        given().cookie("SESSION", cookie)
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "userId": "$id",
                        "name": "$name",
                        "surname": "B",
                        "email": "a@a.com"
                    }
                    """)
                .put("/user-service/usersInfo/$id")
                .then()
                .statusCode(201)


        given().cookie("SESSION", cookie)
                .accept(ContentType.JSON)
                .get("/greetings/api/$id")
                .then()
                .statusCode(200)
                .body("message", startsWith("Hello"))
                .body("message", containsString(name))

    }
}