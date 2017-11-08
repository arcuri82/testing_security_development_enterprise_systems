package org.tsdes.spring.security.database.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by arcuri82 on 08-Nov-17.
 */
@Service
@Transactional
class UserHandler(
        @Autowired
        private val userCrud: UserRepository,
        @Autowired
        private val passwordEncoder: PasswordEncoder
){


    fun createUser(username: String, password: String) : Boolean{

        try {
            val hash = passwordEncoder.encode(password)

            if (userCrud.exists(username)) {
                return false
            }

            val user = User(username, hash)

            userCrud.save(user)

            return true
        } catch (e: Exception){
            return false
        }
    }

}

interface UserRepository : CrudRepository<User, String>
