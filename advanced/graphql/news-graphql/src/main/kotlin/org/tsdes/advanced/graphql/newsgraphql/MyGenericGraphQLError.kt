package org.tsdes.advanced.graphql.newsgraphql

import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation
import java.lang.RuntimeException

/*
    FIXME

    TODO report issue to Kotlin

    Maybe related to following?
    https://youtrack.jetbrains.com/issue/KT-14115#tab=Comments
 */


//class MyGenericGraphQLError(message: String) : RuntimeException(message), GraphQLError {
//
//    override fun getMessage(): String? {
//        return super.message
//    }
//
//    @JsonIgnore
//    override fun getLocations(): List<SourceLocation>? {
//        return null
//    }
//
//    @JsonIgnore
//    override fun getErrorType(): ErrorType? {
//        return null
//    }
//}
