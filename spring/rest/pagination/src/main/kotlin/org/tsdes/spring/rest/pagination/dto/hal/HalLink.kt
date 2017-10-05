package org.tsdes.spring.rest.pagination.dto.hal

import io.swagger.annotations.ApiModelProperty

/*
    Note: a HAL link can have more data, but here I am only
    interested in the "href"
 */
open class HalLink(

        @ApiModelProperty("URL of the link")
        var href: String
)