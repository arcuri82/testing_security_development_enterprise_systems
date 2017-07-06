package org.tsdes.spring.examples.news.constraint

import com.google.common.io.Resources
import java.nio.charset.Charset

/**
 * Created by arcuri82 on 16-Jun-17.
 */
class CountryList {

    companion object {

        val countries: List<String>

        init {
            val url = Resources.getResource(CountryList::class.java, "/country/country_list.txt")
            countries = Resources.readLines(url, Charset.forName("UTF-8"))
        }

        fun isValidCountry(country: String): Boolean {
            return countries.any { s -> s.equals(country, ignoreCase = true) }
        }
    }
}

