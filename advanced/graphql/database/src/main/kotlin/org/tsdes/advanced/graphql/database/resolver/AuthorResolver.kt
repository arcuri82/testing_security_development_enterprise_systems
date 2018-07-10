package org.tsdes.advanced.graphql.database.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.springframework.stereotype.Component
import org.tsdes.advanced.graphql.database.entity.AuthorEntity

@Component
class AuthorResolver : GraphQLResolver<AuthorEntity> {

    fun getId(author: AuthorEntity) = author.id.toString()
}