package org.tsdes.advanced.graphql.database.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.tsdes.advanced.graphql.database.entity.CommentEntity
import org.tsdes.advanced.graphql.database.entity.PostEntity
import org.tsdes.advanced.graphql.database.repository.CommentRepository
import org.tsdes.advanced.graphql.database.repository.PostRepository

@Component
class PostResolver(
        private val postRepository: PostRepository,
        private val commentRepository: CommentRepository
) : GraphQLResolver<PostEntity> {

    fun getId(post: PostEntity) = post.id.toString()


    /**
     * Recall that "comments" are lazily initialized, and here
     * we are not in the same transaction that loaded "post"
     */
    @Transactional
    fun getComments(post: PostEntity): List<CommentEntity> {

        //FIXME: This in theory should work
        //Seems same problem as in https://stackoverflow.com/questions/48037601/lazyinitializationexception-with-graphql-spring#

//        println("------- TRANSACTION: " + TransactionSynchronizationManager.isActualTransactionActive())
//        val reloaded = postRepository.findById(post.id).get()
//        return reloaded.comments


        return commentRepository.findByParentPostId(post.id!!)
    }
}