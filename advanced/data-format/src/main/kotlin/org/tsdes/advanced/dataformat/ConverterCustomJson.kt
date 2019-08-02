package org.tsdes.advanced.dataformat

import java.text.SimpleDateFormat
import java.util.*



class ConverterCustomJson<T: Any>(type: Class<T>) : ConverterImp<T>(type) {


    override fun fromJSon(json: String): T {

        /*
            This is quite tricky to implement, as you need to create a parser
            based on the JSON grammar. If you are interested, you can create
            parsers for custom grammars using JavaCC:
            https://javacc.java.net/
            or ANTLR:
            https://www.antlr.org/
         */

        return super.fromJSon(json)
    }


    override fun toJSon(obj: T): String {

        val buffer = StringBuffer(1024)

        convertToJsonDirectlyByReflection(buffer, obj)

        return buffer.toString()
    }


    private fun convertToJsonDirectlyByReflection(buffer: StringBuffer, obj: Any) {
        /*
            Note 1: this is very basic, and does not handle all the
            corner cases. It is just to let you understand how unmarshalling
            tools work.

            Note 2: this is using reflection, in a similar way that we saw
            for Dependency Injection in PG5100.
         */

        buffer.append("{\n") //open object definition

        /*
            Get the Field properties in the object, but skip all the ones
            with no value, ie null
         */
        val fields = obj::class.java.declaredFields
                .filter { it.apply { isAccessible = true }.get(obj) != null }
                /*
                    "isAccessible = true" is to avoid issues with private fields
                    when doing reflection.
                    Note: being a property, what actually called is "setIsAccessible(true)"
                 */


        for (i in fields.indices) {

            val field = fields[i]


            val value = field.get(obj)

            //print the name
            buffer.append("\"" + field.name + "\":")

            val type = field.type

            if (type.isPrimitive
                    || Int::class.java.isAssignableFrom(type)
                    || Integer::class.java.isAssignableFrom(type)) {
                /*
                    primitive type? just append it
                 */
                buffer.append(value)

            } else if (type == String::class.java) {

                //strings have to be inside ""
                buffer.append("\"" + value + "\"")

            } else if (type == Date::class.java) {

                //"Oct 8, 2016 10:41:28 AM"
                val format = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a")
                val date = format.format(value as Date?)
                buffer.append("\"" + date + "\"")

            } else if (List::class.java.isAssignableFrom(type)) {

                /*
                   collections (eg array, list) should be inside []
                 */

                buffer.append("[\n")
                val list = value as List<*>

                for (j in 0 until list.size) {

                    val e = list[j]

                    if (e != null) {
                        //recursion
                        convertToJsonDirectlyByReflection(buffer, e)
                    }

                    if (j < list.size - 1) {
                        buffer.append(",\n")
                    }
                }
                buffer.append("]\n")

            } else {
                throw IllegalArgumentException("Cannot handle type: $type")
            }

            if (i != fields.size - 1) {
                buffer.append(",")
            }

            buffer.append("\n")
        }

        buffer.append("}\n") //close object definition
    }
}