package org.tsdes.advanced.rest.dto.hal

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import kotlin.math.max
import kotlin.math.min


/*
    A generic DTO in HAL format to represent a list of DTOs,
    with all needed links and pagination info
 */
@ApiModel(description = "Paginated list of resources with HAL links, like to 'next' and 'previous' pages ")
class PageDto<T>(

        @get:ApiModelProperty("The list of resources in the current retrieved page")
        var list: MutableList<T> = mutableListOf(),

        @get:ApiModelProperty("The index of first element in this page")
        var rangeMin: Int = 0,

        @get:ApiModelProperty("The index of the last element of this page")
        var rangeMax: Int = 0,

        @get:ApiModelProperty("The total number of elements")
        var totalSize: Long = 0,

        /*
            Note: these are input parameters for the constructor (ie, no var/val),
            no properties are generated with getters/setters for them.
            The reason is that we need to override their getters/setters,
            and we cannot do it in the constructor.
         */

        next: HalLink? = null,

        previous: HalLink? = null,

        _self: HalLink? = null


) : HalObject() {

    /*
        The following is a what can be called a showing off of
        "coding voyeurism"

        The superclass HalLink holds a set of links.
        In this ListDto, we want some specific links, like "next" and "previous".
        So, we could access them with "_links['next']".
        But repeating such keys every time we access them is not the most
        clean solution. What if we misspell 'next' with 'nxt'?
        Even worse, how to distinguish the case of a misspelled key
        from a missing key? (they both lead to a null from the set).

        So, we want to have "properties" for them, which are statically
        checked at compilation time.
        Fine, but we do not want to replicate data. So, these properties
        need to actual refer to the set in the superclass.
        So? We need to override their "get"/"set" methods.
        Furthermore, as do not want to replicate the data in the
        generated JSON files, we need to explicitly tells Jackson
        to skip those properties with JsonIgnore. Note: "next" is still going to
        be marshaled as part of the marshalling of the "_link" set in the
        superclass.
        Last but not least, we use the default "init" to store the values
        coming from the constructor call into these properties (that have
        the same names).
     */

    @get:JsonIgnore
    var next: HalLink?
        set(value) {
            if (value != null) {
                _links["next"] = value
            } else {
                _links.remove("next")
            }
        }
        get() = _links["next"]


    @get:JsonIgnore
    var previous: HalLink?
        set(value) {
            if (value != null) {
                _links["previous"] = value
            }  else {
                _links.remove("previous")
            }
        }
        get() = _links["previous"]


    @get:JsonIgnore
    var _self: HalLink?
        set(value) {
            if (value != null) {
                _links["self"] = value
            } else {
                _links.remove("self")
            }
        }
        get() = _links["self"]


    init {
        this.next = next
        this.previous = previous
        this._self = _self
    }



    companion object {

        /**
         *  Utility function to build a PageDto where pages are
         *  defined with "offset" and "limit" query parameters
         */
        fun <T> withLinksBasedOnOffsetAndLimitParameters(
                list: MutableList<T> = mutableListOf(),
                rangeMin: Int = 0,
                rangeMax: Int = 0,
                onDb: Long = 0,
                maxFromDb: Int = 0,
                baseUri: UriComponentsBuilder
        ) : PageDto<T>{

            val limit = (rangeMax - rangeMin) + 1

            /*
            Create URL links for "self", "next" and "previous" pages.
            Each page will have up to "limit" NewsDto objects.
            A page is identified by the offset in the list.

            Note: needs to clone the builder, as each call
            like "queryParam" does not create a new one, but
            rather update the existing one
            */

            val self = HalLink(baseUri.cloneBuilder()
                    .queryParam("offset", rangeMin)
                    .queryParam("limit", limit)
                    .build().toString()
            )

            val previous = if ( rangeMin > 0) {
                HalLink(baseUri.cloneBuilder()
                        .queryParam("offset", max(rangeMin - limit, 0))
                        .queryParam("limit", min(limit, rangeMin))
                        .build().toString()
                )
            } else null

            val availableSize = min(maxFromDb, onDb.toInt())

            val next = if (rangeMax + 1 < availableSize) {
                HalLink(baseUri.cloneBuilder()
                        .queryParam("offset", rangeMax + 1)
                        .queryParam("limit", min(limit, availableSize - (rangeMax+1)))
                        .build().toString()
                )
            } else null

            return PageDto(list, rangeMin, rangeMax, onDb, next, previous, self)
        }

    }
}


