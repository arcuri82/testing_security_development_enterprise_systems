package org.tsdes.advanced.graphql.resolver.type


data class PostType(
        var id: Long,
        var authorId: Long,
        var text: String
)