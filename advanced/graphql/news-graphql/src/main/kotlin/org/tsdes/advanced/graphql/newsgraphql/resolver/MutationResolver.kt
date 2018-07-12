package org.tsdes.advanced.graphql.newsgraphql.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.google.common.base.Throwables
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.tsdes.advanced.examplenews.NewsRepository
import org.tsdes.advanced.graphql.newsgraphql.type.InputNewsType
import javax.validation.ConstraintViolationException

@Component
class MutationResolver(
        private val crud: NewsRepository

) : GraphQLMutationResolver {

    fun createNews(input: InputNewsType): String {

        val id = try {
            //FIXME check test with null, and add explanation
            crud.createNews(input.authorId!!, input.text!!, input.country!!)
        } catch (e: Exception) {
            val cause = Throwables.getRootCause(e)
            if(cause is ConstraintViolationException) {
                throw IllegalArgumentException("Violated constraints: ${cause.message}")
            }
            throw e
        }

        return id.toString()
    }

    fun deleteNewsById(pathId: String): Boolean {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return false
        }

        if (!crud.existsById(id)) {
            return false
        }

        crud.deleteById(id)

        return true
    }
}