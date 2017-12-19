package org.tsdes.spring.security.distributedsession.zuul

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
class UserService(
        @Autowired
        private val userCrud: UserRepository,
        @Autowired
        private val passwordEncoder: PasswordEncoder
){


    fun createUser(username: String, password: String, roles: Set<String> = setOf()) : Boolean{

        try {
            val hash = passwordEncoder.encode(password)

            if (userCrud.exists(username)) {
                return false
            }

            val user = UserEntity(username, hash, roles.map{"ROLE_$it"}.toSet())

            userCrud.save(user)

            return true
        } catch (e: Exception){
            return false
        }
    }

}

interface UserRepository : CrudRepository<UserEntity, String>

