package org.tsdes.advanced.rest.guiv2.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@Repository
interface BookRepository : CrudRepository<Book, Long>, BookRepositoryCustom


@Transactional
interface BookRepositoryCustom {

    /**
     * Keyset pagination
     */
    fun getNextPage(size: Int, lastId: Long?, lastReadYear: Int?): List<Book>
}


@Transactional
@Repository
class BookRepositoryImpl(
        val em: EntityManager
) : BookRepositoryCustom {

    override fun getNextPage(size: Int, lastId: Long?, lastReadYear: Int?): List<Book> {

        if (size < 1 || size > 1000) {
            throw IllegalArgumentException("Invalid size value: $size")
        }

        if((lastId==null && lastReadYear!=null) || (lastId!=null && lastReadYear==null)){
            throw IllegalArgumentException("lastId and lastReadYear should be both missing, or both present")
        }

        val query: TypedQuery<Book>
        if (lastId == null) {
            query = em.createQuery(
                    "select b from Book b order by b.year, b.id DESC"
                    , Book::class.java)
        } else {
            query = em.createQuery(
                    "select b from Book b where b.year<?2 or (b.year=?2 and b.id<?1) order by b.year, b.id DESC"
                    , Book::class.java)
            query.setParameter(1, lastId)
            query.setParameter(2, lastReadYear)
        }
        query.maxResults = size

        return query.resultList
    }
}