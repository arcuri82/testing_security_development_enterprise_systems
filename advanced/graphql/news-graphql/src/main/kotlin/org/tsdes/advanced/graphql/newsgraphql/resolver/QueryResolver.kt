package org.tsdes.advanced.graphql.newsgraphql.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestParam
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

    fun news(country: String?, authorId: String?) : List<NewsType>{

        val list = if (country.isNullOrBlank() && authorId.isNullOrBlank()) {
            crud.findAll()
        } else if (!country.isNullOrBlank() && !authorId.isNullOrBlank()) {
            crud.findAllByCountryAndAuthorId(country!!, authorId!!)
        } else if (!country.isNullOrBlank()) {
            crud.findAllByCountry(country!!)
        } else {
            crud.findAllByAuthorId(authorId!!)
        }

        return NewsConverter.transform(list)
    }
}