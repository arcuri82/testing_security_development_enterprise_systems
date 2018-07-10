package org.tsdes.advanced.graphql.database.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.advanced.graphql.database.DatabaseGraphQLApplication
import javax.transaction.Transactional


@RunWith(SpringRunner::class)
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