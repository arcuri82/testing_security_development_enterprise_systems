package org.tsdes.advanced.rest.wiremock

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by arcuri82 on 02-Aug-17.
 */
@XmlRootElement
data class ConversionDto(

        @XmlElement(required = true)
        var from: String? = null,

        @XmlElement(required = true)
        var to: String? = null,

        @XmlElement(required = true)
        var rate: Double? = null
)