package org.tsdes.advanced.graphql.newsgraphql.scalar

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


/*
    JSON does not define how Date objects should be represented, although
    JavaScript does (ISO-8601).
    So, GraphQL by default does not support dates either as basic types.
    We could use a String to transfer dates, but then we lose all the
    static checks on its constraints.
    So, we define a custom-type for GraphQL to represent dates.

    Dates are very common, and it might be that GraphQL libraries do
    provide custom scalars for them by default.
    This is not the case yet for graphql-java, see:
    https://github.com/graphql-java/graphql-java/issues/991

    Anyway, even if one day it will be supported by default, it is still
    interesting to use it as an example of how to handle custom types.
 */

@Component
class MyDateTimeScalar : GraphQLScalarType("MyDateTime", "DataTime scalar", MyDateTimeScalarCoercing())

private class MyDateTimeScalarCoercing : Coercing<ZonedDateTime, String> {


    override fun serialize(input: Any): String {
        if (input is ZonedDateTime) {
            return input.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        }

        val result = convertString(input)
                ?: throw CoercingSerializeException("Invalid value '$input' for ZonedDateTime")

        return result.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }

    override fun parseValue(input: Any): ZonedDateTime {

        return convertString(input)
                ?: throw CoercingParseValueException("Invalid value '$input' for ZonedDateTime")
    }

    override fun parseLiteral(input: Any): ZonedDateTime? {

        if (input !is StringValue){
            return null
        }

        return convertString(input.value)
    }

    private fun convertString(input: Any): ZonedDateTime? {

        if (input is String) {
            return try {
                ZonedDateTime.parse(input)
            } catch (e: DateTimeParseException) {
                null
            }
        }

        return null
    }
}