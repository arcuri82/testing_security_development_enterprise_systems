package org.tsdes.advanced.graphql.database.repository

import org.springframework.stereotype.Service
import org.tsdes.advanced.graphql.database.entity.AuthorEntity
import org.tsdes.advanced.graphql.database.entity.CommentEntity
import org.tsdes.advanced.graphql.database.entity.PostEntity
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Service
class DefaultDataService(
        private val postRepository: PostRepository,
        private val authorRepository: AuthorRepository
) {


    @PostConstruct
    @Transactional
    fun initialize() {

        val a0 = AuthorEntity("Foo", "Bar")
        val a1 = AuthorEntity("John", "Smith")

        val p0 = PostEntity(a0, "Foo is the word!")
        val p1 = PostEntity(a0, "On the many uses of the word Foo")
        val p2 = PostEntity(a1, "On GraphQL")

        val c0 = CommentEntity(a1, p0, "No it is not!")
        val c1 = CommentEntity(a0, p0, "Yes it is!")
        val c2 = CommentEntity(a1, p1, "Please just stop")

        p0.comments.add(c0)
        p0.comments.add(c1)
        p1.comments.add(c2)

        authorRepository.saveAll(listOf(a0, a1))
        postRepository.saveAll(listOf(p0, p1, p2))
    }
}