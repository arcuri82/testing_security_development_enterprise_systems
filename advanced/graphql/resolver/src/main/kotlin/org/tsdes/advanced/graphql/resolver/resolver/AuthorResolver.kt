package org.tsdes.advanced.graphql.resolver.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.springframework.stereotype.Component
import org.tsdes.advanced.graphql.resolver.type.AuthorType

@Component
class AuthorResolver : GraphQLResolver<AuthorType> {

    fun getId(author: AuthorType) = author.id.toString()
}