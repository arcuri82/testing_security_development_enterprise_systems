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
    TODO check when will be supported by default
    https://github.com/graphql-java/graphql-java/issues/991
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