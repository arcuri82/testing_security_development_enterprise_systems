package org.tsdes.advanced.graphql.base

import org.springframework.stereotype.Component

@Component
class UserRepository {

    private val users = mutableMapOf<String, UserDto>()


    init {
        listOf(UserDto("0", "Foo", "Bar", 42),
                UserDto("1", "Joe", "Black", 18),
                UserDto("2", "John", "Smith", 7),
                UserDto("3", "Mario", "Rossi", 25)
        ).forEach { users[it.id] = it }

    }

    fun allUsers(): Collection<UserDto> = users.values

    fun findById(id: String) = users[id]
}