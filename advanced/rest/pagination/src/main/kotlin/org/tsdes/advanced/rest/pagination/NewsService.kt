package org.tsdes.advanced.rest.pagination

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.tsdes.advanced.rest.pagination.entity.Comment
import org.tsdes.advanced.rest.pagination.entity.News
import org.tsdes.advanced.rest.pagination.entity.Vote
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery

/*
    Here we define am injectable singleton bean (Service),
    whose methods are run in a database transaction (Transactional).
    This is practically equivalent to EJBs in JEE.
 */
@Service
@Transactional
class NewsService(
        val em: EntityManager
) {
    /*
        Here I am not using a CRUD repository, but
        rather handling all manually via an JPA Entity Manager.
     */

    fun getNews(id: Long): News? {
        val news = em.find(News::class.java, id)

        if (news != null) {
            //force loading from DB of the LAZY lists
            news.votes.size
            news.comments.size
        }
        return news
    }

    fun getNewsList(country: String?,
                    withComments: Boolean,
                    withVotes: Boolean,
                    limit: Int): List<News> {

        val query: TypedQuery<News>
        if (country == null) {
            query = em.createQuery("select n from News n", News::class.java)
        } else {
            query = em.createQuery("select n from News n where n.country=?1", News::class.java)
            query.setParameter(1, country)
        }
        query.maxResults = limit

        /*
            Those lists are lazily initialized, so they are going to be
            loaded from database only when accessed for the firs time.
            But it has to be done when there is an open session from an
            EntityManager. So, to be on the safe side, we do it here.

            However, also notice that this code, in this particular case,
            is redundant. The DTO transformer, even if executed outside of
            an explicit transaction, can load such data, as it is a read-only
            operation, still done from an Entity whose Entity Manager is
            still active
         */
        val result = query.resultList
        if (withComments) {
            result.forEach { n -> n.comments.size }
        }
        if (withVotes) {
            result.forEach { n -> n.votes.size }
        }

        return result
    }


    fun createNews(text: String, country: String): Long? {

        val news = News(text = text, country = country)

        em.persist(news)

        return news.id
    }

    fun deleteNews(newsId: Long) {
        val news = em.find(News::class.java, newsId)
        if (news != null) {
            em.remove(news)
        }
    }

    fun createComment(newsId: Long, text: String): Long? {

        val news = em.find(News::class.java, newsId)
                ?: throw IllegalArgumentException("News does not exist")

        val comment = Comment(news = news, text = text)

        em.persist(comment)

        /*
            Recall: as I get news from em.find() inside
            this transaction, any change to it will be automatically
            sent to the database.

            If, on the other hand, this came as input to the method (
            eg among the "newsId" and "text") from a non-transactional
            context, then I would need to use em.merge()
         */
        news.comments.add(comment)

        return comment.id
    }

    fun createVote(newsId: Long, user: String): Long? {

        val news = em.find(News::class.java, newsId)
                ?: throw IllegalArgumentException("News does not exist")

        val vote = Vote(user = user, news = news)

        em.persist(vote)

        news.votes.add(vote)

        return vote.id
    }
}
