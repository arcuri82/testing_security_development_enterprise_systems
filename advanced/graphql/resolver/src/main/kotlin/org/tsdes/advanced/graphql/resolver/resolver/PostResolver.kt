package org.tsdes.advanced.graphql.resolver.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.springframework.stereotype.Component
import org.tsdes.advanced.graphql.resolver.DataRepository
import org.tsdes.advanced.graphql.resolver.type.AuthorType
import org.tsdes.advanced.graphql.resolver.type.CommentType
import org.tsdes.advanced.graphql.resolver.type.PostType

@Component
class PostResolver(
        private val repository: DataRepository
) : GraphQLResolver<PostType> {

    fun getId(post: PostType) = post.id.toString()


    fun getAuthor(post: PostType) : AuthorType? {

        return repository.findAuthorById(post.authorId)
    }


    /**
     * Note: this might be an expensive operation, as every single
     * comment in the repository has to be scan
     */
    fun getComments(post: PostType) : List<CommentType>{

        return repository.getComments{it -> it.postId == post.id}
    }
}