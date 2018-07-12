package org.tsdes.advanced.graphql.newsgraphql.type


data class NewsType(
        var newsId: String? = null,
        var authorId: String? = null,
        var text: String? = null,
        var country: String? = null
        //TODO date
)