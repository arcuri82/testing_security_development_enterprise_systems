package org.tsdes.advanced.graphql.resolver.type


data class CommentType(
        var id: Long,
        var authorId: Long,
        var postId: Long,
        var text: String
)