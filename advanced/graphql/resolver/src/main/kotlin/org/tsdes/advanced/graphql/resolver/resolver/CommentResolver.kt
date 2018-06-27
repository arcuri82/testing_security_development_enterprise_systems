package org.tsdes.advanced.graphql.resolver.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.springframework.stereotype.Component
import org.tsdes.advanced.graphql.resolver.DataRepository
import org.tsdes.advanced.graphql.resolver.type.AuthorType
import org.tsdes.advanced.graphql.resolver.type.CommentType
import org.tsdes.advanced.graphql.resolver.type.PostType

@Component
class CommentResolver(
        private val repository: DataRepository
) : GraphQLResolver<CommentType> {

    fun getId(comment: CommentType) = comment.id.toString()

    fun getAuthor(comment: CommentType): AuthorType? {

        return repository.findAuthorById(comment.authorId)
    }

    fun getParentPost(comment: CommentType): PostType? {

        return repository.findPostById(comment.postId)
    }
}