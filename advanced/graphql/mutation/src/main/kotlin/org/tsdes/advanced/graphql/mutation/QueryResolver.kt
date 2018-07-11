package org.tsdes.advanced.graphql.mutation

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class QueryResolver(
        private val repository: UserRepository

) : GraphQLQueryResolver {


    fun all(): List<UserType> = repository.allUsers().toList()

    fun findById(id: String) = repository.findById(id)
}