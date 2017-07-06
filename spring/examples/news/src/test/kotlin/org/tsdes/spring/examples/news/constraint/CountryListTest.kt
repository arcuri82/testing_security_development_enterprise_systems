package org.tsdes.spring.examples.news.constraint

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by arcuri82 on 16-Jun-17.
 */
class CountryListTest {

    @Test
    fun testList() {
        val list = CountryList.countries
        assertTrue("Wrong size: ${list.size}", list.size > 200)
        assertTrue(list.any({ s -> s.equals("Norway", ignoreCase = true) }))
    }
}