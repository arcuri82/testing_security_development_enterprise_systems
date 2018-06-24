package org.tsdes.advanced.examplenews.constraint

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by arcuri82 on 16-Jun-17.
 */
class CountryListTest {

    @Test
    fun testList() {
        /*
            Note how we access fields in the companion objects like
            it was a static value in Java.
         */
        val list = CountryList.countries
        assertTrue("Wrong size: ${list.size}", list.size > 200)
        assertTrue(list.any { it.equals("Norway", ignoreCase = true) })
    }
}