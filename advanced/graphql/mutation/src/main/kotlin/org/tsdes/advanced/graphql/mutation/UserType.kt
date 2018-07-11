package org.tsdes.advanced.graphql.mutation

import graphql.schema.GraphQLInputType


data class UserType (
        var id: String,
        var name: String?,
        var surname: String?,
        var age: Int?
)