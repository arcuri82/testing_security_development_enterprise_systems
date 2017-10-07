package org.tsdes.spring.rest.pagination.dto.hal

import io.swagger.annotations.ApiModelProperty


/*
    HAL (Hypertext Application Language) is a proposal (not a standard, at least not yet...)
    to how to define links in JSON objects
    for HATEOAS ( Hypermedia As The Engine Of Application State).

    Here, I am just using a subset of it.

    More info at:
    https://en.wikipedia.org/wiki/Hypertext_Application_Language
    https://en.wikipedia.org/wiki/HATEOAS
 */
open class HalObject(

        @ApiModelProperty("HAL links")
        var _links: MutableMap<String, HalLink> = mutableMapOf()
)
