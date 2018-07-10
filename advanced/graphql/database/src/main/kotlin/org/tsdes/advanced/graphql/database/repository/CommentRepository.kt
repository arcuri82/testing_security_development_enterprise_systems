package org.tsdes.advanced.graphql.database.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.tsdes.advanced.graphql.database.entity.CommentEntity

@Repository
interface CommentRepository : CrudRepository<CommentEntity, Long>{

    fun findByParentPostId(id: Long) : List<CommentEntity>
}