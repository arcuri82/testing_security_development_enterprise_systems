package org.tsdes.advanced.kotlin

/**
 * Examples of writing code in a Functional Programming (FP) style,
 * avoiding creating local variables, done by chaining functions
 *
 * Created by arcuri82 on 17-Aug-17.
 */
class KotlinFP {

    class Foo{
        fun intialize(){}
        fun doSomething(){}
    }

    //following two methods are equivalent

    fun createFooNoFP() : Foo {

        val foo = Foo()
        foo.intialize()
        foo.doSomething()
        return foo
    }

    fun createFooWithFP() : Foo {

        return Foo().apply { intialize(); doSomething() }
    }


    /*
        following two methods are equivalent.
        They represent the extraction of a text body payload from
        a HTTP response.
        We can write in FP, which is in general good, but not always...
        _nesting_ different methods like .run() and .let() can lead to
        hard to read code...
     */

    fun getHttpBodyBlockNoFP(message: String): String? {

        val lines =  message.split("\n")
        val emptyLineIndex = lines.indexOfFirst({it.isBlank() })

        if (emptyLineIndex < 0 || emptyLineIndex == lines.lastIndex){
            return null
        } else {
            return lines.subList(emptyLineIndex + 1, lines.size).joinToString("\n")
        }
    }


    fun getHttpBodyBlockWithFP(message: String): String? {

        return message.split("\n")
                .run {
                    // this == message.split("\n")
                    // indexOfFirst is called on List<String>
                    indexOfFirst { it.isBlank() } // "it" represents element in list
                            .let {
                                // "this" has not changed, still pointing to List<String>
                                // "it" here is the index returned by indexOfFirst
                                // note that lastIndex is this.lastIndex, on List<String>
                                if (it < 0 || it == lastIndex) return null
                                // subList and size are called on "this", ie List<String>
                                else return subList(it + 1, size).joinToString("\n")
                            }
                }
        /*
           note the total lack of local variables...
           however it can become difficult to read...
        */
    }
}