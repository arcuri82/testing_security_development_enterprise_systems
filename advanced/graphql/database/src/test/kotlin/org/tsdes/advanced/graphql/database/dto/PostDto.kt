package org.tsdes.advanced.graphql.database.dto


data class PostDto(
        var id: String? = null,
        var author: AuthorDto? = null,
        var text: String? = null,
        var comments: MutableList<CommentDto>? = mutableListOf()
)