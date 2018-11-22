package org.tsdes.advanced.examplenews.constraint

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


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
        assertTrue(list.size > 200, "Wrong size: ${list.size}")
        assertTrue(list.any { it.equals("Norway", ignoreCase = true) })
    }
}