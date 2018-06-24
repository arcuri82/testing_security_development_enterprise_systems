package org.tsdes.advanced.dataformat


/**
 * Note: by default, in Kotlin a generic T would bind to "Any?", which
 * allows nullable types (eg, "String?").
 * If we want to prevent null, we need "T: Any"
 */
interface Converter<T: Any> {

    fun toXML(obj: T): String

    fun fromXML(xml: String): T

    fun toJSon(obj: T): String

    fun fromJSon(json: String): T


    /*
        Transforming strings directly from XML to JSon (and vice-versa) would
        be more efficient. But piping together existing methods is very quick
        and easy, and perfectly valid solution when performance is not a major
        constraint.

        Recall that these default methods in this interface could be overwritten
        if necessary
     */


    fun fromJSonToXml(json: String): String {

        val obj = fromJSon(json)

        return toXML(obj)
    }

    fun fromXmlToJSon(xml: String): String {

        val obj = fromXML(xml)

        return toJSon(obj)
    }
}