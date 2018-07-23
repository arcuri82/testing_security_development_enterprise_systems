package org.tsdes.advanced.graphql.database.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.tsdes.advanced.graphql.database.entity.CommentEntity
import org.tsdes.advanced.graphql.database.entity.PostEntity
import org.tsdes.advanced.graphql.database.repository.PostRepository

@Component
class PostResolver(
        private val postRepository: PostRepository
) : GraphQLResolver<PostEntity> {

    fun getId(post: PostEntity) = post.id.toString()


    /**
     * Recall that "comments" are lazily initialized, and here
     * we are not in the same transaction that loaded "post"
     */
    @Transactional
    fun getComments(post: PostEntity): List<CommentEntity> {

        val reloaded = postRepository.findById(post.id).get()

        //recall to force loading here inside the joined transaction
        reloaded.comments.size
        return reloaded.comments
    }
}