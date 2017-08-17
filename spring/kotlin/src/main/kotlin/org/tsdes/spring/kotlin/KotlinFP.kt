package org.tsdes.spring.kotlin

/**
 * Created by arcuri82 on 17-Aug-17.
 */
class KotlinFP {

    fun withFP() {


    }


    fun getHttpBodyBlock(message: String): String? {

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

    fun getHttpBodyBlockNoFP(message: String): String? {

        val tokens =  message.split("\n")
        val emptyLineIndex = tokens.indexOfFirst({line -> line.isBlank() })

        if (emptyLineIndex < 0 || emptyLineIndex == tokens.lastIndex){
            return null
        } else {
            return tokens.subList(emptyLineIndex + 1, tokens.size).joinToString("\n")
        }
    }

    class Foo(){
        fun intialize(){}
        fun doSomething(){}
    }

    fun createFoo() : Foo{

        val foo = Foo()
        foo.intialize()
        foo.doSomething()
        return foo
    }

    fun createFooWithFP() : Foo {

        return Foo().apply { intialize(); doSomething() }
    }
}