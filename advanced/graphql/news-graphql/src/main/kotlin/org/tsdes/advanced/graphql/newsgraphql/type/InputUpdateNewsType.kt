package org.tsdes.advanced.graphql.newsgraphql.type

import java.time.ZonedDateTime


open class InputUpdateNewsType(
        authorId: String? = null,
        text: String? = null,
        country: String? = null,
        var creationTime: ZonedDateTime? = null
) : InputNewsType(authorId, text, country)