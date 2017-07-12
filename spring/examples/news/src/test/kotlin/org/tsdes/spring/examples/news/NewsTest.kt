package org.tsdes.spring.examples.news

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import javax.validation.ConstraintViolationException

/**
 * Created by arcuri82 on 16-Jun-17.
 */
@RunWith(SpringRunner::class)
@DataJpaTest //this take care to start and re-init an embedded database at each test execution
class NewsTest {

    @Autowired
    private lateinit var crud: NewsRepository

    @Test
    fun testInitialization(){
        assertNotNull(crud)
    }

    @Test
    fun testCreate() {

        assertEquals(0, crud.count())

        val id = crud.createNews("author", "someText", "Norway")

        assertEquals(1, crud.count())
        assertEquals(id, crud.findOne(id).id)
    }

    @Test
    fun testDelete() {

        assertEquals(0, crud.count())

        val id = crud.createNews("author", "text", "Norway")
        assertTrue(crud.exists(id))
        assertTrue(crud.findAll().any { n -> n.id == id })
        assertEquals(1, crud.count())

        crud.delete(id)

        assertFalse(crud.exists(id))
        assertFalse(crud.findAll().any { n -> n.id == id })
        assertEquals(0, crud.count())
    }

    @Test
    fun testGet() {

        val author = "author"
        val text = "someText"
        val country = "Norway"

        val id = crud.createNews(author, text, country)
        val news = crud.findOne(id)

        assertEquals(author, news.authorId)
        assertEquals(text, news.text)
        assertEquals(country, news.country)
    }

    @Test
    fun testUpdate() {

        val text = "someText"

        val id = crud.createNews("author", text, "Norway")
        assertEquals(text, crud.findOne(id).text)

        val updated = "new updated text"

        crud.updateText(id, updated)
        assertEquals(updated, crud.findOne(id).text)
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
            fail()
        } catch (e: ConstraintViolationException) {
            //expected
        }
    }

    @Test
    fun testInvalidText() {
        try {
            crud.createNews("author", "", "Norway")
            fail()
        } catch (e: ConstraintViolationException) {
            //expected
        }
    }

    @Test
    fun testInvalidCountry() {
        try {
            crud.createNews("author", "text", "Foo")
            fail()
        } catch (e: ConstraintViolationException) {
            //expected
        }
    }
}


@SpringBootApplication
class TestApplication