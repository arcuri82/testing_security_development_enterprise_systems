package org.tsdes.advanced.dataformat


/**
 * Note: by default, in Kotlin a generic T would bind to "Any?", which
 * allows nullable types (eg, "String?").
 * If we want to prevent null, we need "T: Any"
 */
interface Converter<T: Any> {

    fun toXml(obj: T): String

    fun fromXml(xml: String): T

    fun toJson(obj: T): String

    fun fromJson(json: String): T


    /*
        Transforming strings directly from XML to JSON (and vice-versa) would
        be more efficient. But piping together existing methods is very quick
        and easy, and perfectly valid solution when performance is not a major
        constraint.

        Recall that these default methods in this interface could be overwritten
        if necessary
     */


    fun fromJsonToXml(json: String): String {

        val obj = fromJson(json)

        return toXml(obj)
    }

    fun fromXmlToJson(xml: String): String {

        val obj = fromXml(xml)

        return toJson(obj)
    }
}