package org.tsdes.advanced.rest.pagination

import org.tsdes.advanced.rest.dto.hal.ListDto
import org.tsdes.advanced.rest.pagination.dto.CommentDto
import org.tsdes.advanced.rest.pagination.dto.NewsDto
import org.tsdes.advanced.rest.pagination.dto.VoteDto
import org.tsdes.advanced.rest.pagination.entity.Comment
import org.tsdes.advanced.rest.pagination.entity.News
import org.tsdes.advanced.rest.pagination.entity.Vote
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

        val dtoList: MutableList<NewsDto> = newsList.stream()
                .skip(offset.toLong()) // this is a good example of how streams simplify coding
                .limit(limit.toLong())
                .map { transform(it, withComments, withVotes) }
                .toList().toMutableList()


        return ListDto(
                list = dtoList,
                rangeMin = offset,
                rangeMax = offset + dtoList.size - 1,
                totalSize = newsList.size
        )
    }
}