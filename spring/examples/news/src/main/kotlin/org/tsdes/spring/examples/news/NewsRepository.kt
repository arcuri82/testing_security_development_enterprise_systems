package org.tsdes.spring.examples.news

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by arcuri82 on 16-Jun-17.
 */
@Repository
interface NewsRepository : CrudRepository<NewsEntity, Long>, NewsRepositoryCustom {

    fun findAllByCountry(country: String) : Iterable<NewsEntity>

    fun findAllByAuthorId(authorId: String) : Iterable<NewsEntity>

    fun findAllByCountryAndAuthorId(country: String, authorId: String) : Iterable<NewsEntity>
}

@Transactional
interface NewsRepositoryCustom {

    fun createNews(authorId: String, text: String, country: String) : Long

    fun updateText(newsId: Long, text: String): Boolean

    fun update( newsId: Long,
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
open class NewsRepositoryImpl : NewsRepositoryCustom{

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun createNews(authorId: String, text: String, country: String) : Long {
        val entity = NewsEntity(authorId, text, ZonedDateTime.now(), country)
        em.persist(entity)
        return entity.id!!
    }

    override fun updateText(newsId: Long, text: String): Boolean {
        val news = em.find(NewsEntity::class.java, newsId) ?: return false
        news.text = text
        return true
    }

    override fun update( newsId: Long,
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