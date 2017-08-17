package org.tsdes.spring.kotlin

/**
 * Created by arcuri82 on 17-Aug-17.
 */
class KotlinNull {

    fun bar(){

        var foo = "foo"
        foo = "changed"
        //foo = null // doesn't compile

        //note the "?" after the type
        var bar : String? = "foo"
        bar = null
    }

    // note the ?
    fun startWithFoo(s: String?) : Boolean {

        //doesn't compile
        //return s.startsWith("foo")

        //elvis operator
        return s?.startsWith("foo") ?: false
    }
}