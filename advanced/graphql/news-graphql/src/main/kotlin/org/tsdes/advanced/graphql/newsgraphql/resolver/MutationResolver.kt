package org.tsdes.advanced.graphql.newsgraphql.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.google.common.base.Throwables
import graphql.execution.DataFetcherResult
import graphql.servlet.GenericGraphQLError
import org.springframework.stereotype.Component
import org.tsdes.advanced.examplenews.NewsRepository
import org.tsdes.advanced.graphql.newsgraphql.type.InputNewsType
import org.tsdes.advanced.graphql.newsgraphql.type.InputUpdateNewsType
import javax.validation.ConstraintViolationException

@Component
class MutationResolver(
        private val crud: NewsRepository

) : GraphQLMutationResolver {

    fun createNews(input: InputNewsType): DataFetcherResult<String> {

        val id = try {
            /*
                the fields in 'input' cannot be null, because, if they were,
                this code would never be reached, as validation already failed
                when parsing the GraphQL request
             */
            crud.createNews(input.authorId!!, input.text!!, input.country!!)
        } catch (e: Exception) {
            val cause = Throwables.getRootCause(e)
            val msg = if (cause is ConstraintViolationException) {
                "Violated constraints: ${cause.message}"
            }else {
                "${e.javaClass}: ${e.message}"
            }
            return DataFetcherResult<String>(null, listOf(GenericGraphQLError(msg)))
        }

        return DataFetcherResult(id.toString(), listOf())
    }

    fun updateNewsById(pathId: String, input: InputUpdateNewsType): DataFetcherResult<Boolean> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return DataFetcherResult<Boolean>(null, listOf(
                    GenericGraphQLError("No News with id $pathId exists")))
        }

        if (!crud.existsById(id)) {
            return DataFetcherResult<Boolean>(null, listOf(
                    GenericGraphQLError("No News with id $id exists")))
        }

        try {
            crud.update(id, input.text!!, input.authorId!!, input.country!!, input.creationTime!!)
        } catch (e: Exception) {
            val cause = Throwables.getRootCause(e)
            if (cause is ConstraintViolationException) {
                return DataFetcherResult<Boolean>(null, listOf(
                        GenericGraphQLError("Violated constraints: ${cause.message}")))
            }
            throw e
        }

        return DataFetcherResult(true, listOf())
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