package org.tsdes.advanced.graphql.database.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.tsdes.advanced.graphql.database.entity.PostEntity

@Repository
interface PostRepository : CrudRepository<PostEntity, Long>