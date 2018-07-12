package org.tsdes.advanced.graphql.newsgraphql.type


data class InputNewsType(
        var authorId: String? = null,
        var text: String? = null,
        var country: String? = null
)