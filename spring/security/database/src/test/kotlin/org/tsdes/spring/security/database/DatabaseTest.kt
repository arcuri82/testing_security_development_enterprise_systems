package org.tsdes.spring.security.database

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.tsdes.spring.security.database.db.UserRepository
import org.tsdes.spring.security.database.db.UserService

/**
 * Created by arcuri82 on 08-Nov-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE)
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
class DatabaseTest {

    @Autowired
    private lateinit var userHandler: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder


    @Before
    fun clean(){
        userRepository.deleteAll()
    }

    @Test
    fun testCreateUser(){

        val name = "foo"
        val pwd = "bar"
        val role = "USER"

        val created = userHandler.createUser(name, pwd, setOf(role))

        assertTrue(created)

        val user = userRepository.findOne(name)

        assertEquals(name, user.username)
        assertNotEquals(pwd, user.password)

        assertTrue(passwordEncoder.matches(pwd, user.password))
    }

}