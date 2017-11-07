package org.tsdes.spring.microservice.gateway.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping(path = arrayOf("/messages"))
class ServiceApplication (

        /*
            There are two main ways of doing dependency injection:
            (1) by field (using reflection), or (2) by constructor.
            The end result is practically the same, with maybe
            one main difference that with (2) constructor injection
            you need to be more careful of dependency cycles.

            In Java, I do strongly prefer (1), as it is way less
            verbose. Few people though prefer (2), because it allows
            them to use mock frameworks (eg Mockito) when writing
            tests. As I am usually against (apart from few cases)
            the use of mock frameworks for testing (as far too often they lead
            to brittle test suites that quickly become expensive to maintain),
            I am actually considering the inability to use mocking in (1)
            as a "feature", and not as a limitation.
            However, if you are doing TDD, and you actually write tens
            of thousands of unit tests even for most trivial code
            like setters/getters, then you want those unit tests as fast
            as possible, and so (2) + mocks make sense in that context.


            In Kotlin, it is bit different, as (2) is actually less
            verbose (no "lateinit"), and also allows you to declare the
            reference as "val" instead of "var" (and using "val" is better).
            However, you might still consider (1) if you want to
            completely shield your project from mock framework abuse.
            Still, though, as mock frameworks have some uses in some
            cases, (2) can make more sense in Kotlin.
         */

        @Autowired
        private val crud: MessageRepository
){

    private val id = (System.getenv("TSDES_SERVICE_ID") ?: "Undefined").trim()

    /*
        Note: for simplicity in this example I am using the @Entity
        directly as DTO, which is usually not a good practice.
        Furthermore, not doing any input validation or exception
        handling here...
    */


    @GetMapping(produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun get() : ResponseEntity<List<MessageEntity>> {

        return ResponseEntity.ok(crud.findAll().toList())
    }


    @DeleteMapping
    fun delete() : ResponseEntity<Void>{
        crud.deleteAll()

        return ResponseEntity.status(204).build()
    }


    @PostMapping(
            consumes = arrayOf(MediaType.TEXT_PLAIN_VALUE)
    )
    fun create(
            @RequestBody
            message: String
    ) : ResponseEntity<Void>{

        crud.save(MessageEntity(id, message))

        return ResponseEntity.status(201).build()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ServiceApplication::class.java, *args)
}
