package org.tsdes.advanced.dataformat.data

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
data class Post(
        @field:XmlElement(nillable = false, required = true)
        var content: String? = null,

        @field:XmlAttribute
        var author: String? = null,

        @field:XmlAttribute
        var votes: Int? = null
)