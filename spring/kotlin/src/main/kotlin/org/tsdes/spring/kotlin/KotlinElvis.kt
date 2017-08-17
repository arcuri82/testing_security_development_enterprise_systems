package org.tsdes.spring.kotlin

/**
 * Created by arcuri82 on 17-Aug-17.
 */
class KotlinElvis {

    class Link(var next: Link?, var s: String?)

    fun fiveNextIsFoo(link : Link?) : Boolean{

        return link?.next
                ?.next
                ?.next
                ?.next
                ?.next
                ?.s?.equals("foo")
            ?: false
    }
}