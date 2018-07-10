package org.tsdes.advanced.graphql.database.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import org.tsdes.advanced.graphql.database.entity.PostEntity
import org.tsdes.advanced.graphql.database.repository.PostRepository

@Component
class QueryResolver(
        private val repository: PostRepository

) : GraphQLQueryResolver {


    fun allPosts(): List<PostEntity> = repository.findAll().toList()
}