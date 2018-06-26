package org.tsdes.advanced.graphql.base

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class Query(
        private val repository: UserRepository
)
    : GraphQLQueryResolver {


    fun all(): List<UserDto> = repository.allUsers().toList()
}