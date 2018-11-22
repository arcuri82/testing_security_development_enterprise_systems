package org.tsdes.advanced.graphql.database.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.graphql.database.DatabaseGraphQLApplication
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [DatabaseGraphQLApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DefaultDataServiceTest{

    @Autowired
    private lateinit var postRepository: PostRepository


    @Test
    @Transactional
    fun testDefaultData(){

        val posts = postRepository.findAll().toList()

        assertEquals(3, posts.size)

        assertEquals(3, posts.flatMap { it.comments }.size)
    }
}