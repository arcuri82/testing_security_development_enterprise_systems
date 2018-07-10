package org.tsdes.advanced.graphql.database.dto


data class CommentDto(
        var id: String? = null,
        var author: AuthorDto? = null,
        var parentPost: PostDto? = null,
        var text: String? = null
)