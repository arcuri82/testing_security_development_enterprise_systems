package org.tsdes.advanced.graphql.newsgraphql.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.google.common.base.Throwables
import org.springframework.stereotype.Component
import org.tsdes.advanced.examplenews.NewsRepository
import org.tsdes.advanced.graphql.newsgraphql.MyGenericGraphQLError
import org.tsdes.advanced.graphql.newsgraphql.type.InputNewsType
import org.tsdes.advanced.graphql.newsgraphql.type.InputUpdateNewsType
import javax.validation.ConstraintViolationException

@Component
class MutationResolver(
        private val crud: NewsRepository

) : GraphQLMutationResolver {

    fun createNews(input: InputNewsType): String {

        val id = try {
            /*
                the fields in 'input' cannot be null, because, if they were,
                this code would never be reached, as validation already failed
                when parsing the GraphQL request
             */
            crud.createNews(input.authorId!!, input.text!!, input.country!!)
        } catch (e: Exception) {
            val cause = Throwables.getRootCause(e)
            if(cause is ConstraintViolationException) {
                throw MyGenericGraphQLError("Violated constraints: ${cause.message}")
            }
            throw MyGenericGraphQLError(e.message)
        }

        return id.toString()
    }

    fun updateNewsById(pathId: String, input: InputUpdateNewsType): Boolean {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            throw MyGenericGraphQLError("No News with id $pathId exists")
        }

        if (!crud.existsById(id)) {
            throw MyGenericGraphQLError("No News with id $id exists")
        }

        try {
            crud.update(id, input.text!!, input.authorId!!, input.country!!, input.creationTime!!)
        } catch (e: Exception) {
            val cause = Throwables.getRootCause(e)
            if(cause is ConstraintViolationException) {
                throw MyGenericGraphQLError("Violated constraints: ${cause.message}")
            }
            throw e
        }

        return true
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