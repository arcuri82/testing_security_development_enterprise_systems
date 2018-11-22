package org.tsdes.advanced.dataformat

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.tsdes.advanced.dataformat.data.Post
import org.tsdes.advanced.dataformat.data.TopPosts
import java.util.*

class ConverterTest{

    @Test
    fun testXml() {

        val converter = ConverterImp(TopPosts::class.java)
        val topPosts = getTopPosts()

        val xml = converter.toXML(topPosts)
        println(xml)

        val res = converter.fromXML(xml)

        verifyEquivalence(topPosts, res)
    }


    @Test
    fun testJSon() {

        val converter = ConverterImp(TopPosts::class.java)

        val topPosts = getTopPosts()

        val json = converter.toJSon(topPosts)
        println(json)

        val res = converter.fromJSon(json)

        verifyEquivalence(topPosts, res)
    }

    @Test
    fun testToFromJSonXml() {

        val converter = ConverterImp(TopPosts::class.java)
        val topPosts = getTopPosts()

        var json = converter.toJSon(topPosts)

        val xml = converter.fromJSonToXml(json)

        json = converter.fromXmlToJSon(xml)

        val res = converter.fromJSon(json)

        verifyEquivalence(topPosts, res)
    }


    @Test
    fun testSize() {

        val converter = ConverterImp(TopPosts::class.java)
        val topPosts = getTopPosts()

        val json = converter.toJSon(topPosts)
        val xml = converter.toXML(topPosts)

        //usually, XML is larger than JSon
        assertTrue(xml.length > json.length)
    }



    @Test
    fun testCustomJson() {

        val converter = ConverterCustomJson<TopPosts>(TopPosts::class.java)
        val topPosts = getTopPosts()

        val custom = converter.toJSon(topPosts)
        println("Custom JSON:\n$custom")

        val backWithGson = converter.fromJSon(custom)

        verifyEquivalence(topPosts, backWithGson)
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