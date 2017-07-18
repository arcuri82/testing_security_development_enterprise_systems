package org.tsdes.spring.rest.newsrest.dto

import org.tsdes.spring.examples.news.NewsEntity
import java.util.*
import java.util.stream.Collectors

/*
    It might be tempting to use the @Entity objects directly as DTOs.
    That is not advisable.
    It might work for small applications developed by one single person,
    but it can become a problem (eg, maintainability and de-coupling) for
    larger projects.

    So here we need a converter from @Entity to DTO
 */
class NewsConverter {

    companion object {

        fun transform(entity: NewsEntity): NewsDto {

            return NewsDto(
                    id = entity.id?.toString(),
                    authorId = entity.authorId,
                    text = entity.text,
                    country = entity.country,
                    creationTime = entity.creationTime
            )
        }

        fun transform(entities: Iterable<NewsEntity>): List<NewsDto> {
            return entities.map { transform(it) }
        }
    }
}