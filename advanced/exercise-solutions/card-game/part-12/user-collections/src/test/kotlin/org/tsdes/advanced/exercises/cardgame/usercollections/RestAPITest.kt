package org.tsdes.advanced.exercises.cardgame.usercollections

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.exercises.cardgame.usercollections.db.UserRepository
import org.tsdes.advanced.exercises.cardgame.usercollections.db.UserService
import org.tsdes.advanced.exercises.cardgame.usercollections.dto.Command
import org.tsdes.advanced.exercises.cardgame.usercollections.dto.PatchUserDto
import org.tsdes.advanced.rest.dto.WrappedResponse
import wiremock.com.fasterxml.jackson.databind.ObjectMapper
import javax.annotation.PostConstruct


@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [(RestAPITest.Companion.Initializer::class)])
internal class RestAPITest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    companion object {

        private lateinit var wiremockServer: WireMockServer

        @BeforeAll
        @JvmStatic
        fun initClass() {
            wiremockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort().notifier(ConsoleNotifier(true)))
            wiremockServer.start()


            val dto = WrappedResponse(code = 200, data = FakeData.getCollectionDto()).validated()
            val json = ObjectMapper().writeValueAsString(dto)

            wiremockServer.stubFor(
                    WireMock.get(WireMock.urlMatching("/api/cards/collection_.*"))
                            .willReturn(WireMock.aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json; charset=utf-8")
                                    .withBody(json)))
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of("cardServiceAddress: localhost:${wiremockServer.port()}")
                        .applyTo(configurableApplicationContext.environment)
            }
        }
    }

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/user-collections"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }


    @BeforeEach
    fun initTest() {
        userRepository.deleteAll()
    }


    @Test
    fun testAccessControl() {

        val id = "foo"

        given().get("/$id").then().statusCode(401)
        given().put("/$id").then().statusCode(401)
        given().patch("/$id").then().statusCode(401)

        given().auth().basic("bar", "123")
                .get("/$id")
                .then()
                .statusCode(403)
    }


    @Test
    fun testCreateUser() {
        val id = "foo"

        given().auth().basic(id, "123")
                .put("/$id")
                .then()
                .statusCode(201)

        assertTrue(userRepository.existsById(id))
    }

    @Test
    fun testBuyCard() {

        val userId = "foo"
        val cardId = "c00"

        given().auth().basic(userId, "123").put("/$userId").then().statusCode(201)

        given().auth().basic(userId, "123")
                .contentType(ContentType.JSON)
                .body(PatchUserDto(Command.BUY_CARD, cardId))
                .patch("/$userId")
                .then()
                .statusCode(200)

        val user = userService.findByIdEager(userId)!!
        assertTrue(user.ownedCards.any { it.cardId == cardId })
    }


    @Test
    fun testOpenPack() {

        val userId = "foo"
        given().auth().basic(userId, "123").put("/$userId").then().statusCode(201)

        val before = userService.findByIdEager(userId)!!
        val totCards = before.ownedCards.sumBy { it.numberOfCopies }
        val totPacks = before.cardPacks
        assertTrue(totPacks > 0)

        given().auth().basic(userId, "123")
                .contentType(ContentType.JSON)
                .body(PatchUserDto(Command.OPEN_PACK))
                .patch("/$userId")
                .then()
                .statusCode(200)

        val after = userService.findByIdEager(userId)!!
        assertEquals(totPacks - 1, after.cardPacks)
        assertEquals(totCards + UserService.CARDS_PER_PACK,
                after.ownedCards.sumBy { it.numberOfCopies })
    }


    @Test
    fun testMillCard() {

        val userId = "foo"
        given().auth().basic(userId, "123").put("/$userId").then().statusCode(201)

        val before = userRepository.findById(userId).get()
        val coins = before.coins

        given().auth().basic(userId, "123")
                .contentType(ContentType.JSON)
                .body(PatchUserDto(Command.OPEN_PACK))
                .patch("/$userId")
                .then()
                .statusCode(200)

        val between = userService.findByIdEager(userId)!!
        val n = between.ownedCards.sumBy { it.numberOfCopies }


        val cardId = between.ownedCards[0].cardId!!
        given().auth().basic(userId, "123")
                .contentType(ContentType.JSON)
                .body(PatchUserDto(Command.MILL_CARD, cardId))
                .patch("/$userId")
                .then()
                .statusCode(200)

        val after = userService.findByIdEager(userId)!!
        assertTrue(after.coins > coins)
        assertEquals(n - 1, after.ownedCards.sumBy { it.numberOfCopies })
    }
}