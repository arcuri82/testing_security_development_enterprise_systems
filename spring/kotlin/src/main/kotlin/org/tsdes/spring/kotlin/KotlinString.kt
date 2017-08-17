package org.tsdes.spring.kotlin

/**
 * Created by arcuri82 on 17-Aug-17.
 */
class KotlinString {

    fun aboutStrings(){

        val multiLineString = """
            <foo>
               Some message
            </foo>
        """

        val x = 5
        val s = "Use \$ to interpolate, eg x=$x"
        print(s)
        //The print(s) does output the following:
        //Use $ to interpolate, eg x=5
    }
}

fun main(args: Array<String>) {
    KotlinString().aboutStrings()
}