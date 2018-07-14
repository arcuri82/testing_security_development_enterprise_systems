package org.tsdes.advanced.graphql.dto


open class DtoGraphQL<T> (

    var data: T? = null,

    var errors: List<ErrorDto>? = mutableListOf()
)