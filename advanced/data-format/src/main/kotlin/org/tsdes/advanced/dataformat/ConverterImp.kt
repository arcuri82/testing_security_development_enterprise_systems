package org.tsdes.advanced.dataformat

import com.google.gson.Gson
import java.io.StringReader
import java.io.StringWriter
import javax.xml.bind.JAXBContext


open class ConverterImp<T: Any>(
        private val type: Class<T>

) : Converter<T> {


    override fun toXml(obj: T): String {

        val context = JAXBContext.newInstance(type)

        val m = context.createMarshaller()

        val writer = StringWriter()

        m.marshal(obj, writer)

        return writer.toString()
    }

    override fun fromXml(xml: String): T {

        val context = JAXBContext.newInstance(type)
        val u = context.createUnmarshaller()

        val reader = StringReader(xml)

        return u.unmarshal(reader) as T
    }

    override fun toJson(obj: T): String {

        //see https://github.com/google/gson

        return Gson().toJson(obj)
    }

    override fun fromJson(json: String): T {

        return Gson().fromJson(json, type)
    }
}