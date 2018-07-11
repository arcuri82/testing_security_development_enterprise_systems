package org.tsdes.advanced.graphql.database.dto

import org.tsdes.advanced.graphql.dto.DtoGraphQL
import org.tsdes.advanced.graphql.dto.ErrorDto


class GraphQLResponseDto(data: QueryDto? = null, errors: List<ErrorDto>? = null)
    : DtoGraphQL<QueryDto>(data, errors)