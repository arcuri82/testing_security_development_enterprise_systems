package org.tsdes.advanced.examplenews

import com.google.common.base.Throwables
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.validation.ConstraintViolationException

/**
 * Created by arcuri82 on 16-Jun-17.
 */
@ExtendWith(SpringExtension::class) // needed to let Spring doing all the dependency injection and bean initialization
@DataJpaTest //this take care to start and re-init an embedded database at each test execution
@Transactional(propagation = Propagation.NEVER)
class NewsTest {

    /*
        To do dependency injection in Spring, can use @Autowired.
        Also JEE @Inject would work.
     */

    @Autowired
    private lateinit var crud: NewsRepository

    @BeforeEach
    fun cleanDatabase(){
        crud.deleteAll()
    }

    @Test
    fun testInitialization(){
        assertNotNull(crud)
    }

    @Test
    fun testCreate() {

        assertEquals(0, crud.count())

        val id = crud.createNews("author", "someText", "Norway")

        assertEquals(1, crud.count())
        assertEquals(id, crud.findById(id).get().id)
    }

    @Test
    fun testDelete() {

        assertEquals(0, crud.count())

        val id = crud.createNews("author", "text", "Norway")
        assertTrue(crud.existsById(id))
        assertTrue(crud.findAll().any { n -> n.id == id })
        assertEquals(1, crud.count())

        crud.deleteById(id)

        assertFalse(crud.existsById(id))
        assertFalse(crud.findAll().any { n -> n.id == id })
        assertEquals(0, crud.count())
    }

    @Test
    fun testGet() {

        val author = "author"
        val text = "someText"
        val country = "Norway"

        val id = crud.createNews(author, text, country)
        val news = crud.findById(id).get()

        assertEquals(author, news.authorId)
        assertEquals(text, news.text)
        assertEquals(country, news.country)
    }

    @Test
    fun testUpdate() {

        val text = "someText"

        val id = crud.createNews("author", text, "Norway")
        assertEquals(text, crud.findById(id).get().text)

        val updated = "new updated text"

        crud.updateText(id, updated)
        assertEquals(updated, crud.findById(id).get().text)
    }


    private fun createSomeNews() {
        crud.createNews("a", "text", "Norway")
        crud.createNews("a", "other text", "Norway")
        crud.createNews("a", "more text", "Sweden")
        crud.createNews("b", "text", "Norway")
        crud.createNews("b", "yet another text", "Iceland")
        crud.createNews("c", "text", "Iceland")
    }

    @Test
    fun testFindAll() {

        assertEquals(0, crud.findAll().count())
        createSomeNews()

        assertEquals(6, crud.findAll().count())
    }

    @Test
    fun testGetAllByCountry() {

        assertEquals(0,crud.count())
        createSomeNews()

        assertEquals(3, crud.findAllByCountry("Norway").count())
        assertEquals(1, crud.findAllByCountry("Sweden").count())
        assertEquals(2, crud.findAllByCountry("Iceland").count())
    }

    @Test
    fun testGetAllByAuthor() {

        assertEquals(0, crud.count())
        createSomeNews()

        assertEquals(3, crud.findAllByAuthorId("a").count())
        assertEquals(2, crud.findAllByAuthorId("b").count())
        assertEquals(1, crud.findAllByAuthorId("c").count())
    }

    @Test
    fun testGetAllByCountryAndAuthor() {

        assertEquals(0, crud.count())
        createSomeNews()

        assertEquals(2, crud.findAllByCountryAndAuthorId("Norway", "a").count())
        assertEquals(1, crud.findAllByCountryAndAuthorId("Sweden", "a").count())
        assertEquals(0, crud.findAllByCountryAndAuthorId("Iceland", "a").count())
        assertEquals(1, crud.findAllByCountryAndAuthorId("Norway", "b").count())
        assertEquals(0, crud.findAllByCountryAndAuthorId("Sweden", "b").count())
        assertEquals(1, crud.findAllByCountryAndAuthorId("Iceland", "b").count())
        assertEquals(0, crud.findAllByCountryAndAuthorId("Norway", "c").count())
        assertEquals(0, crud.findAllByCountryAndAuthorId("Sweden", "c").count())
        assertEquals(1, crud.findAllByCountryAndAuthorId("Iceland", "c").count())
    }

    @Test
    fun testInvalidAuthor() {
        try {
            crud.createNews("", "text", "Norway")
            fail<Any>()
        } catch (e: Exception) {
            //expected
            assertTrue(Throwables.getRootCause(e) is ConstraintViolationException)
        }
    }


    @Test
    fun testInvalidCountry() {
        try {
            crud.createNews("author", "text", "Foo")
            fail<Any>()
        } catch (e: Exception) {
            //expected
            assertTrue(Throwables.getRootCause(e) is ConstraintViolationException)
        }
    }


    @Test
    fun testInvalidText() {
        try {
            crud.createNews("author", "", "Norway")
            fail<Any>()
        } catch (e: Exception) {
            //expected
            assertTrue(Throwables.getRootCause(e) is ConstraintViolationException)
        }
    }


    @Test
    fun testTooLongText() {

        val text = "a".repeat(1025)

        try {
            crud.createNews("author", text, "Norway")
            fail<Any>()
        } catch (e: Exception) {
            //expected
            assertTrue(Throwables.getRootCause(e) is ConstraintViolationException)
        }
    }

    @Test
    fun testUpdateWithTooLongText() {

        val text = "text"
        val id =crud.createNews("author", text, "Norway")
        assertEquals(1, crud.count())

        val updated = "a".repeat(1025)

        assertThrows(Exception::class.java){crud.updateText(id, updated)}

        assertNotEquals(updated, crud.findById(id).get().text)
    }
}

/*
    When we run the tests, Spring JUnit Runner will scan the classpath
    to find a Spring application entry point.
    So here, we provide one by just having a class annotated with
    @SpringBootApplication

    Note that Spring Boot analyze what provided on the classpath (ie, the
    dependencies in the pom file) to decide what to configure (eg if there
    is the need to start a web server).
 */
@SpringBootApplication
class TestApplication