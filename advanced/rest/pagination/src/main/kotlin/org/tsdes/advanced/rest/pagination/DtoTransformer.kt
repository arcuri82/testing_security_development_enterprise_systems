package org.tsdes.advanced.rest.pagination

import org.springframework.web.util.UriComponentsBuilder
import org.tsdes.advanced.rest.dto.hal.PageDto
import org.tsdes.advanced.rest.pagination.dto.CommentDto
import org.tsdes.advanced.rest.pagination.dto.NewsDto
import org.tsdes.advanced.rest.pagination.dto.VoteDto
import org.tsdes.advanced.rest.pagination.entity.Comment
import org.tsdes.advanced.rest.pagination.entity.News
import org.tsdes.advanced.rest.pagination.entity.Vote


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
                  onDb: Long,
                  maxFromDb: Int,
                  withComments: Boolean,
                  withVotes: Boolean,
                  baseUri: UriComponentsBuilder): PageDto<NewsDto> {

        val dtoList: MutableList<NewsDto> = newsList
                .map { transform(it, withComments, withVotes) }
                .toMutableList()


        return PageDto.withLinksBasedOnOffsetAndLimitParameters(
                list = dtoList,
                rangeMin = offset,
                rangeMax = offset + limit - 1,
                onDb = onDb,
                maxFromDb = maxFromDb,
                baseUri = baseUri
        )
    }
}