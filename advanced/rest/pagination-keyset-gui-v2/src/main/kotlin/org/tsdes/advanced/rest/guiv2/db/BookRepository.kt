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
    fun getNextPage(size: Int, keysetId: Long?, keysetYear: Int?): List<Book>
}


@Transactional
@Repository
class BookRepositoryImpl(
        val em: EntityManager
) : BookRepositoryCustom {

    override fun getNextPage(size: Int, keysetId: Long?, keysetYear: Int?): List<Book> {

        if (size < 1 || size > 1000) {
            throw IllegalArgumentException("Invalid size value: $size")
        }

        if((keysetId==null && keysetYear!=null) || (keysetId!=null && keysetYear==null)){
            throw IllegalArgumentException("keysetId and keysetYear should be both missing, or both present")
        }

        val query: TypedQuery<Book>
        if (keysetId == null) {
            query = em.createQuery(
                    "select b from Book b order by b.year DESC, b.id DESC"
                    , Book::class.java)
        } else {
            query = em.createQuery(
                    "select b from Book b where b.year<?2 or (b.year=?2 and b.id<?1) order by b.year DESC, b.id DESC"
                    , Book::class.java)
            query.setParameter(1, keysetId)
            query.setParameter(2, keysetYear)
        }
        query.maxResults = size

        return query.resultList
    }
}