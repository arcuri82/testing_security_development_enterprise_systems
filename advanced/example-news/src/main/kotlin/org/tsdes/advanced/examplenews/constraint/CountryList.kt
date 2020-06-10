package org.tsdes.advanced.examplenews.constraint

import com.google.common.io.Resources
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Created by arcuri82 on 16-Jun-17.
 */
class CountryList {

    companion object {

        /*
            Companion Objects can be considered as singleton within the class.
            They are used to represent the equivalent of static state in Kotlin.
         */

        val countries: List<String>

        init {
            /*
                The "init" method is called when the object is created.

                Here using Guava library to help reading list of countries from resource file.

                Note the "::class.java" instead of ".class" to refer to a Class object
             */
            val url = Resources.getResource(CountryList::class.java, "/country/country_list.txt")

            /*
                "country" is defined as "val", which is a constant. This assignment here works
                only because we are inside a "init" method
             */
            countries = Resources.readLines(url,  StandardCharsets.UTF_8)
        }

        fun isValidCountry(country: String): Boolean {
            return countries.any { it.equals(country, ignoreCase = true) }
        }
    }
}

