package org.tsdes.advanced.graphql.database.dto


data class QueryDto(
        var allPosts: MutableList<PostDto>? = mutableListOf()
)