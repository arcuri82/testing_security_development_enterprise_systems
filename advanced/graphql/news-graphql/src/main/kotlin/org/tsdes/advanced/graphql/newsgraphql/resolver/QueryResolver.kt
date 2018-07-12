package org.tsdes.advanced.graphql.newsgraphql.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import org.tsdes.advanced.examplenews.NewsRepository
import org.tsdes.advanced.examplenews.constraint.CountryList
import org.tsdes.advanced.graphql.newsgraphql.NewsConverter
import org.tsdes.advanced.graphql.newsgraphql.type.NewsType

@Component
class QueryResolver(
        private val crud: NewsRepository

): GraphQLQueryResolver {


    fun countries() = CountryList.countries


    fun newsById(inputId: String) : NewsType? {

        val id: Long
        try {
            id = inputId.toLong()
        } catch (e: Exception) {
            return null
        }

        val entity = crud.findById(id).orElse(null) ?: return null

        return NewsConverter.transform(entity)
    }

    fun news() : List<NewsType>{

        //TODO filter

        return NewsConverter.transform(crud.findAll())
    }
}