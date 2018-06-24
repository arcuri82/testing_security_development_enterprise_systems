package org.tsdes.advanced.dataformat.data

import java.util.*
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class TopPosts(

        @field:XmlElement(required = true)
        var retrievedTime: Date? = null,

        @field:XmlElementWrapper(name = "postList", required = true)
        @field:XmlElement(name = "post", required = true)
        var posts: MutableList<Post>? = null
) {

    fun addPost(p: Post) {
        if (posts == null) {
            posts = ArrayList()
        }
        posts!!.add(p)
    }
}