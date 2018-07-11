package org.tsdes.advanced.graphql.mutation

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class UserRepository {

    private val users = mutableMapOf<String, UserType>()

    private val counter = AtomicInteger(0)

    /*
        only used in tests
     */
    fun clear(){
        users.clear()
        counter.set(0)
    }

    fun allUsers(): Collection<UserType> = users.values

    fun findById(id: String) = users[id]

    fun create(name: String, surname: String, age: Int) : String {

        val id = counter.getAndIncrement().toString()

        val user = UserType(id, name, surname, age)

        users[id] = user

        return id
    }


    fun update(user: UserType) : Boolean {
        if(! users.containsKey(user.id)){
           return false
        }

        users[user.id]  = user

        return true
    }
}