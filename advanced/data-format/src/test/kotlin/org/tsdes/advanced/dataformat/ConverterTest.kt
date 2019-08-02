package org.tsdes.advanced.dataformat

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.tsdes.advanced.dataformat.data.Post
import org.tsdes.advanced.dataformat.data.TopPosts
import java.util.*
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

class ConverterTest{

    @Test
    fun testXml() {

        val converter = ConverterImp(TopPosts::class.java)
        val topPosts = getTopPosts()

        val xml = converter.toXml(topPosts)
        println(xml)

        val res = converter.fromXml(xml)

        verifyEquivalence(topPosts, res)
    }


    @Test
    fun testJson() {

        val converter = ConverterImp(TopPosts::class.java)

        val topPosts = getTopPosts()

        val json = converter.toJson(topPosts)
        println(json)

        val res = converter.fromJson(json)

        verifyEquivalence(topPosts, res)
    }

    @Test
    fun testToFromJsonXml() {

        val converter = ConverterImp(TopPosts::class.java)
        val topPosts = getTopPosts()

        var json = converter.toJson(topPosts)

        val xml = converter.fromJsonToXml(json)

        json = converter.fromXmlToJson(xml)

        val res = converter.fromJson(json)

        verifyEquivalence(topPosts, res)
    }


    @Test
    fun testSize() {

        val converter = ConverterImp(TopPosts::class.java)
        val topPosts = getTopPosts()

        val json = converter.toJson(topPosts)
        val xml = converter.toXml(topPosts)

        //usually, XML is larger than JSON
        assertTrue(xml.length > json.length)
    }



    @Test
    fun testCustomJson() {

        val converter = ConverterCustomJson(TopPosts::class.java)
        val topPosts = getTopPosts()

        val custom = converter.toJson(topPosts)
        println("Custom JSON:\n$custom")

        val backWithGson = converter.fromJson(custom)

        verifyEquivalence(topPosts, backWithGson)
    }


    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    private data class X (
            var x: Long = 0
    )

    @Test
    fun testLong(){

        val x = X(Long.MAX_VALUE)
        val converter = ConverterImp(X::class.java)

        val toXml = converter.toXml(x)
        val fromXml = converter.fromXml(toXml)
        assertEquals(x, fromXml)

        val toJson = converter.toJson(x)
        val fromJson = converter.fromJson(toJson)
        assertEquals(x, fromJson)

        /*
            Note: max long would be 9223372036854775807 , ie, 2^63
            The JSON will contain the right value here, but technically it would be
            wrong, as JSON does not have 64bit signed longs, but rather just doubles.
            For example, a browser like Chrome would not be able to hold such value,
            and could "silently" round it to: 9223372036854776000.
            You can try by pasting 9223372036854775807 into the JavaScript console in
            Developer Tools.
         */
    }


    private fun verifyEquivalence(a: TopPosts, b: TopPosts) {

        assertNotNull(a)
        assertNotNull(b)
        assertEquals(a.retrievedTime.toString(), b.retrievedTime.toString())
        assertEquals(a.posts!!.size, b.posts!!.size)

        for (i in 0 until a.posts!!.size) {
            assertEquals(a.posts!![i], b.posts!![i])
        }
    }

    private fun getTopPosts(): TopPosts {

        val topPosts = TopPosts(Date())

        val a = Post(content = "A post", author = "First", votes = 2)
        topPosts.addPost(a)

        val b = Post(content = "A second post with no attributes")
        topPosts.addPost(b)

        return topPosts
    }
}