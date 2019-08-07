package org.tsdes.advanced.rest.pagination

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.rest.dto.hal.PageDto
import org.tsdes.advanced.rest.pagination.dto.CommentDto
import org.tsdes.advanced.rest.pagination.dto.NewsDto
import org.tsdes.advanced.rest.pagination.dto.VoteDto
import org.tsdes.advanced.rest.pagination.entity.News
import java.util.*


@Repository
interface Rep : CrudRepository<News, Long>

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(PaginationApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaginationRestTest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var rep: Rep


    @BeforeEach
    @AfterEach
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/news"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        rep.deleteAll()
    }

    @Test
    fun testBaseCreateNews() {

        val text = "some text"
        val country = "Norway"

        val location = given().body(NewsDto(null, text, country))
                .contentType(ContentType.JSON)
                .post()
                .then()
                .statusCode(201)
                .extract().header("location")

        given().get()
                .then()
                .statusCode(200)
                .body("list.size()", equalTo(1))
                .body("totalSize", equalTo(1))

        given().basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("text", equalTo(text))
                .body("country", equalTo(country))
    }


    private fun createNews(text: String, country: String) {

        given().body(NewsDto(null, text, country))
                .contentType(ContentType.JSON)
                .post()
                .then()
                .statusCode(201)
    }

    private fun createSeveralNews(n: Int): Int {

        for (i in 0 until n) {
            createNews("$i", "Norway")
        }

        return n
    }

    /**
     * Extract the "text" fields from all the News
     */
    private fun getTexts(selfDto: PageDto<*>): MutableSet<String> {

        val values = HashSet<String>()
        selfDto.list.stream()
                .map{ (it as Map<String,String>)["text"] }
                .forEach { if(it!=null){values.add(it)} }

        return values
    }

    private fun assertContainsTheSame(a: Collection<*>, b: Collection<*>) {
        assertEquals(a.size.toLong(), b.size.toLong())
        a.forEach { assertTrue(b.contains(it)) }
        b.forEach { assertTrue(a.contains(it)) }
    }


    @Test
    fun testSelfLink() {

        val n = createSeveralNews(30)
        val limit = 10

        val listDto = given()
                .queryParam("limit", limit)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .`as`(PageDto::class.java)

        assertEquals(n, listDto.totalSize.toInt())
        assertEquals(0, listDto.rangeMin)
        assertEquals(limit - 1, listDto.rangeMax)

        assertNull(listDto.previous)
        assertNotNull(listDto.next)
        assertNotNull(listDto._self)

        //read again using self link
        val selfDto = given()
                .basePath("")
                .get(listDto._self!!.href)
                .then()
                .statusCode(200)
                .extract()
                .`as`(PageDto::class.java)

        val first = getTexts(listDto)
        val self = getTexts(selfDto)

        assertContainsTheSame(first, self)
    }


    @Test
    fun testNextLink() {

        val n = createSeveralNews(30)
        val limit = 10

        var listDto: PageDto<*> = given()
                .queryParam("limit", limit)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .`as`(PageDto::class.java)

        assertEquals(n, listDto.totalSize.toInt())
        assertNotNull(listDto.next!!.href)

        val values = getTexts(listDto)
        var next : String? = listDto.next?.href

        var counter = 0

        //read pages until there is still a "next" link
        while (next != null) {

            counter++

            val beforeNextSize = values.size

            listDto = given()
                    .basePath("")
                    .get(next)
                    .then()
                    .statusCode(200)
                    .extract()
                    .`as`(PageDto::class.java)

            values.addAll(getTexts(listDto))

            assertEquals(beforeNextSize + limit, values.size)
            assertEquals(counter * limit, listDto.rangeMin)
            assertEquals(listDto.rangeMin + limit - 1, listDto.rangeMax)

            next = listDto.next?.href
        }

        assertEquals(n.toLong(), values.size.toLong())
    }

    @Test
    fun testJsonIgnore() {

        createSeveralNews(30)
        val limit = 10

        given().queryParam("limit", limit)
                .get()
                .then()
                .statusCode(200)
                .body("_links.next", notNullValue())
                .body("next", nullValue())
    }

    @Test
    fun textPreviousLink() {

        val n = createSeveralNews(30)
        val limit = 10

        val listDto = given()
                .queryParam("limit", limit)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .`as`(PageDto::class.java)

        val first = getTexts(listDto)

        //read next page
        val nextDto = given()
                .basePath("")
                .get(listDto.next!!.href)
                .then()
                .statusCode(200)
                .extract()
                .`as`(PageDto::class.java)

        val next = getTexts(nextDto)
        // check that an element of next page was not in the first page
        assertTrue(!first.contains(next.iterator().next()))

        /*
            The "previous" page of the "next" page should be the
            first "self" page, ie

            self.next.previous == self
         */
        val previousDto = given()
                .basePath("")
                .get(nextDto.previous!!.href)
                .then()
                .statusCode(200)
                .extract()
                .`as`(PageDto::class.java)

        val previous = getTexts(previousDto)
        assertContainsTheSame(first, previous)
    }


    private fun createNewsWithCommentAndVote() {
        val newsLocation = given().body(NewsDto(null, "some text", "Sweden"))
                .contentType(ContentType.JSON)
                .post()
                .then()
                .statusCode(201)
                .extract().header("location")

        given().basePath("")
                .body(VoteDto(user = "a user"))
                .contentType(ContentType.JSON)
                .post("$newsLocation/votes")
                .then()
                .statusCode(201)

        given().basePath("")
                .body(CommentDto(null, "a comment"))
                .contentType(ContentType.JSON)
                .post("$newsLocation/comments")
                .then()
                .statusCode(201)
    }


    private fun getWithExpand(expandType: String, expectedComments: Int, expectedVotes: Int) {

        given().queryParam("expand", expandType)
                .get()
                .then()
                .statusCode(200)
                .body("list.size()", equalTo(1))
                .body("list[0].comments.id.size()", equalTo(expectedComments))
                .body("list[0].votes.id.size()", equalTo(expectedVotes))
                .body("_links.self.href", containsString("expand=$expandType"))
    }

    @Test
    fun testExpandNone() {

        createNewsWithCommentAndVote()
        getWithExpand("NONE", 0, 0)
    }

    @Test
    fun testExpandALL() {

        createNewsWithCommentAndVote()
        getWithExpand("ALL", 1, 1)
    }

    @Test
    fun testExpandComments() {

        createNewsWithCommentAndVote()
        getWithExpand("COMMENTS", 1, 0)
    }

    @Test
    fun testExpandVotes() {

        createNewsWithCommentAndVote()
        getWithExpand("VOTES", 0, 1)
    }


    @Test
    fun testNegativeOffset() {

        given().queryParam("offset", -1)
                .get()
                .then()
                .statusCode(400)
    }

    @Test
    fun testInvalidOffset() {

        given().queryParam("offset", 0)
                .get()
                .then()
                .statusCode(200)

        given().queryParam("offset", 1)
                .get()
                .then()
                .statusCode(400)
    }

    @Test
    fun testInvalidLimit() {

        given().queryParam("limit", 0)
                .get()
                .then()
                .statusCode(400)
    }


    @Test
    fun testInvalidExpand() {

        /*
            Slightly tricky: in the code we use "expand" as a Java
            enumeration. But, in the URI request, it is just a string.
            So what happens if the string does not match any valid value
            in the enum? Technically, it should be a 4xx user error.
            The actual code might vary depending on the server.
         */

        given().queryParam("expand", "foo")
                .get()
                .then()
                .statusCode(400)


        /*
            however, what if you just use a param that does not exist?
            it just gets ignored...
            so the behavior is actually quite inconsistent.

            The main problem here is that, if you just misspell a query
            parameter, it might just get ignored with no warning.
         */

        given().queryParam("bar", "foo")
                .get()
                .then()
                .statusCode(200)


        /*
            So, this is an issue. If we misspell an existing
            query parameter (eg, note the missing "d"), by default
            it will be just ignored
         */
        given().queryParam("expan", "ALL")
                .get()
                .then()
                .statusCode(200)

        /*
            As ignored, the check against the enum would not be done
         */
        given().queryParam("expan", "foo")
                .get()
                .then()
                .statusCode(200)
    }
}