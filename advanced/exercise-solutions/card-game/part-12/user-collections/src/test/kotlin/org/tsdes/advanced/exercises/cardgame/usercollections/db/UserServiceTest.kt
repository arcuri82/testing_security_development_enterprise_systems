package org.tsdes.advanced.exercises.cardgame.usercollections.db

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.exercises.cardgame.usercollections.CardService
import org.tsdes.advanced.exercises.cardgame.usercollections.model.Collection


@Profile("UserServiceTest")
@Primary
@Service
class FakeCardService : CardService(){

    override fun fetchData() {
        val dto = FakeData.getCollectionDto()
        super.collection = Collection(dto)
    }
}



@ActiveProfiles("UserServiceTest")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
internal class UserServiceTest{

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun initTest(){
        userRepository.deleteAll()
    }


    @Test
    fun testCreateUser(){

        val id = "foo"

        userService.registerNewUser(id)

        assertTrue(userRepository.existsById(id))
    }

}