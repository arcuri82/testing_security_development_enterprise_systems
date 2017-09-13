package org.tsdes.spring.examples.news

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 *  Recall CRUD means: Create, Read, Update and Delete.
 *
 *  By using @Repository annotation on an interface that extends  CrudRepository,
 *  Spring will give a bean initialized to do operation on the given Entity
 *  in the database, like save(), exists(), findAll(), etc.
 *  So a lot of free stuff with a single line of code.
 *
 *  Furthermore, instead of creating JPQL queries manually, and methods in a EJB to
 *  call them, here Spring can automatically generate code in the proxied classes based
 *  on the name of the functions.
 *  For example, if you define a method findAllByCountry (which is just a signature in
 *  this interface), Spring analyse the method name and will create a method that does
 *  query on database for all the entities from the given input country.
 *  If you misspell a property, you will get runtime errors.
 *  So it is important to have tests for them (which you should have anyway...)
 *
 * Created by arcuri82 on 16-Jun-17.
 */
@Repository
interface NewsRepository : CrudRepository<NewsEntity, Long>, NewsRepositoryCustom {

    fun findAllByCountry(country: String): Iterable<NewsEntity>

    fun findAllByAuthorId(authorId: String): Iterable<NewsEntity>

    /*
        You can have more complex queries by using connecting words like "And".
        This is very convenient for simple queries, but not really for complex ones.
     */
    fun findAllByCountryAndAuthorId(country: String, authorId: String): Iterable<NewsEntity>
}

/*
    If we want to add custom operations to the @Repository which we implement
    by ourselves, then it is bit more tricky.

    We first need to create an interface for these new operations, let the @Repository
    interface extend it, and then we need to have a concrete class for it.

    Note the use of @Transactional to put each call into a transaction.
 */

@Transactional
interface NewsRepositoryCustom {

    fun createNews(authorId: String, text: String, country: String): Long

    fun updateText(newsId: Long, text: String): Boolean

    fun update(newsId: Long,
               text: String,
               authorId: String,
               country: String,
               creationTime: ZonedDateTime): Boolean
}

/*
    IMPORTANT: this class has to have the same name X of Repository interface + "Impl"
    See http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.single-repository-behaviour

    Furthermore, as it is not marked with any Spring annotation, Kotlin makes it final by default, which
    does crash Spring (because it needs to create a proxy bean for it). So have to explicitly add "open"
*/
open class NewsRepositoryImpl : NewsRepositoryCustom {

    /*
        To operate manually on the database with JPA, we can inject a reference
        to EntityManager.

        Note the use of "lateinit".
        The field is marked as non-nullable, but we do not initialize it.
        This would lead to a compilation error.
        However, the field will be assigned with dependency injection by Spring
        once the constructor call is completed.
        So, here were are just telling the Kotlin compiler that this field "em"
        is not null, but we are going to initialize it at a later stage.
     */

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun createNews(authorId: String, text: String, country: String): Long {
        val entity = NewsEntity(authorId, text, ZonedDateTime.now(), country)
        em.persist(entity)
        return entity.id!!
    }

    override fun updateText(newsId: Long, text: String): Boolean {
        val news = em.find(NewsEntity::class.java, newsId) ?: return false
        news.text = text
        return true
    }

    override fun update(newsId: Long,
                        text: String,
                        authorId: String,
                        country: String,
                        creationTime: ZonedDateTime): Boolean {
        val news = em.find(NewsEntity::class.java, newsId) ?: return false
        news.text = text
        news.authorId = authorId
        news.country = country
        news.creationTime = creationTime
        return true
    }
}