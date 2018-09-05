package org.tsdes.advanced.graphql.mutation


data class UserType (
        var id: String,
        var name: String?,
        var surname: String?,
        var age: Int?
)