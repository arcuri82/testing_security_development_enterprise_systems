package org.tsdes.spring.rest.pagination

import org.tsdes.spring.rest.pagination.dto.base.CommentDto
import org.tsdes.spring.rest.pagination.dto.base.NewsDto
import org.tsdes.spring.rest.pagination.dto.base.VoteDto
import org.tsdes.spring.rest.pagination.dto.collection.ListDto
import org.tsdes.spring.rest.pagination.entity.Comment
import org.tsdes.spring.rest.pagination.entity.News
import org.tsdes.spring.rest.pagination.entity.Vote
import kotlin.streams.toList


object DtoTransformer {

    fun transform(vote: Vote): VoteDto {
        return VoteDto(vote.id, vote.user)
    }

    fun transform(comment: Comment): CommentDto {
        return CommentDto(comment.id, comment.text)
    }

    fun transform(news: News,
                  withComments: Boolean,
                  withVotes: Boolean): NewsDto {

        val dto = NewsDto(news.id, news.text, news.country)

        if (withComments) {
            news.comments.stream()
                    .map { transform(it) }
                    .forEach { dto.comments.add(it) }
        }

        if (withVotes) {
            news.votes.stream()
                    .map { transform(it) }
                    .forEach { dto.votes.add(it) }
        }

        return dto
    }


    /**
     * This creates a HAL dto, but with the links (self, next, previous)
     * that still have to be set
     */
    fun transform(newsList: List<News>,
                  offset: Int,
                  limit: Int,
                  withComments: Boolean,
                  withVotes: Boolean): ListDto<NewsDto> {

        var dtoList: List<NewsDto> = newsList.stream()
                .skip(offset.toLong()) // this is a good example of how streams simplify coding
                .limit(limit.toLong())
                .map { transform(it, withComments, withVotes) }
                .toList()


        return ListDto<NewsDto>(
                list = dtoList,
                rangeMin = offset,
                rangeMax = offset + dtoList.size -1,
                totalSize = newsList.size
        )
    }
}