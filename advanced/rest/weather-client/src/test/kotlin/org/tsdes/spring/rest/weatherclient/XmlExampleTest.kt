package org.tsdes.spring.rest.weatherclient

import automated.metno.Weather
import com.google.common.io.Resources
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.tsdes.spring.xmlandjson.ConverterImpl

/**
 * Created by arcuri82 on 14-Jun-17.
 */
class XmlExampleTest {

    @Test
    fun testParsing() {

        val url = Resources.getResource("metno_example.xml")
        val xml = Resources.toString(url, Charsets.UTF_8)
        assertNotNull(xml)

        val converter = ConverterImpl(Weather::class.java, "/textforecast.xsd")
        val weather = converter.fromXML(xml)

        //Data sent by Meteorologisk Institutt (http://met.no/) does not even adhere to their own schemas...
        assertNull(weather)
    }
}