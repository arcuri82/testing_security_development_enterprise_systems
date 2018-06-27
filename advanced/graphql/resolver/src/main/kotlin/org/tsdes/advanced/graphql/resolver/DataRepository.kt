package org.tsdes.advanced.graphql.resolver

import org.springframework.stereotype.Component
import org.tsdes.advanced.graphql.resolver.type.AuthorType
import org.tsdes.advanced.graphql.resolver.type.CommentType
import org.tsdes.advanced.graphql.resolver.type.PostType

@Component
class DataRepository {

    private val authors = mutableMapOf<Long, AuthorType>()

    private val posts = mutableMapOf<Long, PostType>()

    private val comments = mutableMapOf<Long, CommentType>()

    init {
        listOf(AuthorType(0, "Foo", "Bar"),
                AuthorType(1, "John", "Smith"))
                .forEach { authors[it.id] = it }

        listOf(PostType(0, 0, "Foo is the word!"),
                PostType(1, 0, "On the many uses of the word Foo"),
                PostType(2, 1, "On GraphQL")
        ).forEach { posts[it.id] = it }

        listOf(CommentType(0, 1, 0, "No it is not!"),
                CommentType(1, 0, 0, "Yes it is!"),
                CommentType(2, 1, 1, "Please just stop")
        ).forEach { comments[it.id] = it }
    }


    fun getAllPosts() = posts.values

    fun findPostById(id: Long) = posts[id]

    fun findAuthorById(id: Long) = authors[id]

    fun getComments(customFilter: ((CommentType) -> Boolean)? = null): List<CommentType> {

        if (customFilter != null) {
            return comments.values.filter(customFilter).toList()
        }

        return comments.values.toList()
    }
}