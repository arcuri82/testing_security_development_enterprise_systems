package org.tsdes.advanced.graphql.newsgraphql

import org.tsdes.advanced.examplenews.NewsEntity
import org.tsdes.advanced.graphql.newsgraphql.type.InputNewsType
import org.tsdes.advanced.graphql.newsgraphql.type.NewsType

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

        fun transform(entity: NewsEntity): NewsType {

            return NewsType(
                    newsId = entity.id?.toString(),
                    authorId = entity.authorId,
                    text = entity.text,
                    country = entity.country
                    //creationTime = entity.creationTime
            )
        }

        fun transform(entities: Iterable<NewsEntity>): List<NewsType> {
            return entities.map { transform(it) }
        }
    }
}