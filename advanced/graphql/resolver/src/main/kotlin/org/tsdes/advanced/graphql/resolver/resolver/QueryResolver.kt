package org.tsdes.advanced.graphql.resolver.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import org.tsdes.advanced.graphql.resolver.DataRepository
import org.tsdes.advanced.graphql.resolver.type.PostType

@Component
class QueryResolver(
        private val repository: DataRepository

) : GraphQLQueryResolver {


    fun allPosts(): List<PostType> = repository.getAllPosts().toList()
}