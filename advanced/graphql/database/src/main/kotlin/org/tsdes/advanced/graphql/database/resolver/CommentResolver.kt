package org.tsdes.advanced.graphql.database.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.springframework.stereotype.Component
import org.tsdes.advanced.graphql.database.entity.CommentEntity

@Component
class CommentResolver : GraphQLResolver<CommentEntity> {

    fun getId(comment: CommentEntity) = comment.id.toString()

}