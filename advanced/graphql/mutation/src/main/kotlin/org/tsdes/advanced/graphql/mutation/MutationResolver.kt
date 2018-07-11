package org.tsdes.advanced.graphql.mutation

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
class MutationResolver(
        private val repository: UserRepository

) : GraphQLMutationResolver {

    fun create(name: String, surname: String, age: Int) : String {

        return repository.create(name, surname, age)
    }

    fun update(user : UserType) : Boolean {
        return repository.update(user)
    }
}