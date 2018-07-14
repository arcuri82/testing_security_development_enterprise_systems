package org.tsdes.advanced.graphql.dto


data class ErrorDto (

    var message: String? = null,

    var locations: List<LocationDto>? = mutableListOf(),

    //Note: not fully correct, as elements can be strings and integers
    var path: List<String>? = mutableListOf()
)