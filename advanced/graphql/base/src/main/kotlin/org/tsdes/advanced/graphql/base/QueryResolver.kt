package org.tsdes.advanced.graphql.base

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class QueryResolver(
        private val repository: UserRepository
)
    : GraphQLQueryResolver {


    fun all(): List<UserType> = repository.allUsers().toList()
}