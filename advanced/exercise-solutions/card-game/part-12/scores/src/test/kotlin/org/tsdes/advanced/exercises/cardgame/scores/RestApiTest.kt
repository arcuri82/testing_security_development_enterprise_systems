package org.tsdes.advanced.exercises.cardgame.scores

import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.exercises.cardgame.scores.db.UserStatsRepository
import org.tsdes.advanced.rest.dto.PageDto
import javax.annotation.PostConstruct


@ActiveProfiles("FakeData")
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(Application::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class RestApiTest{

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var repository: UserStatsRepository

    @PostConstruct
    fun init(){
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    val page : Int = 10

    @Test
    fun testGetPage() {

        RestAssured.given().accept(ContentType.JSON)
                .get("/api/scores")
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(page))
    }


    @Test
    fun testAllPages(){

        val read = mutableSetOf<String>()

        var page = RestAssured.given().accept(ContentType.JSON)
                .get("/api/scores")
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(page))
                .extract().body().jsonPath().getObject("data",object: TypeRef<PageDto<Map<String, Object>>>(){})
        read.addAll(page.list.map { it["userId"].toString()})

        checkOrder(page)

        while(page.next != null){
            page = RestAssured.given().accept(ContentType.JSON)
                    .get(page.next)
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getObject("data",object: TypeRef<PageDto<Map<String, Object>>>(){})
            read.addAll(page.list.map { it["userId"].toString()})
            checkOrder(page)
        }

        val total = repository.count().toInt()

        //recall that sets have unique elements
        assertEquals(total, read.size)
    }

    private fun checkOrder(page: PageDto<Map<String, Object>>) {
        for (i in 0 until page.list.size - 1) {
            val ascore = page.list[i]["score"].toString().toInt()
            val bscore = page.list[i + 1]["score"].toString().toInt()
            val aid = page.list[i]["userId"].toString()
            val bid = page.list[i + 1]["userId"].toString()
            assertTrue(ascore >= bscore)
            if (ascore == bscore) {
                assertTrue(aid > bid)
            }
        }
    }
}