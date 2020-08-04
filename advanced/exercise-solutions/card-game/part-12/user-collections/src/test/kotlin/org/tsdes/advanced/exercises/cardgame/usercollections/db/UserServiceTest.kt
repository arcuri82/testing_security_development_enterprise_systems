package org.tsdes.advanced.exercises.cardgame.usercollections.db

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.exercises.cardgame.usercollections.CardService


@Profile("UserServiceTest")
@Primary
@Service
class FakeCardService : CardService(){

}



@ActiveProfiles("UserServiceTest")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
internal class UserServiceTest{


}