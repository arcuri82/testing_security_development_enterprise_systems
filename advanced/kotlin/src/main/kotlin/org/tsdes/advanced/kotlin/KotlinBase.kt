package org.tsdes.advanced.kotlin

/**
 * Compare this class with its equivalent called JavaBase
 *
 * Created by arcuri82 on 17-Aug-17.
 */
//"public" is default scope
class KotlinBase {

    //"public" is default scope
    fun startWithFoo(s: String)
            : Boolean  //return type is specified at the end after ":"
    {
        val foo = "foo"  //no need for ";" at the end

        //type is implicit at compilation time, but you can specify it
        //if you want, eg:
        //   val foo: String = "foo"

        /*
            Do not need to worry of "s.startsWith" throwing a NPE,
            because compiler checks that caller of "startWithFoo"
            is not null
         */

        return s.startsWith(foo)
    }
}