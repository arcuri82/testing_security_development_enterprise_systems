package org.tsdes.advanced.graphql.base


data class UserDto(
        var id: String,
        var name: String?,
        var surname: String?,
        var age: Int?
)