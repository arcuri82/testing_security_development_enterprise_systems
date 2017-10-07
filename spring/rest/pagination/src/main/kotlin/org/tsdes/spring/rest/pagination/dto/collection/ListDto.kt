package org.tsdes.spring.rest.pagination.dto.collection

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.tsdes.spring.rest.pagination.dto.hal.HalLink
import org.tsdes.spring.rest.pagination.dto.hal.HalObject


/*
    A generic DTO in HAL format to represent a list of DTOs,
    with all needed links and pagination info
 */
@ApiModel(description = "Paginated list of resources with HAL links, like to 'next' and 'previous' pages ")
class ListDto<T>(

        @get:ApiModelProperty("The list of resources in the current retrieved page")
        var list: MutableList<T> = mutableListOf(),

        @get:ApiModelProperty("The index of first element in this page")
        var rangeMin: Int = 0,

        @get:ApiModelProperty("The index of the last element of this page")
        var rangeMax: Int = 0,

        @get:ApiModelProperty("The total number of elements in all pages")
        var totalSize: Int = 0,

        next: HalLink? = null,

        previous: HalLink? = null,

        _self: HalLink? = null


) : HalObject() {

    @get:JsonIgnore
    var next: HalLink?
        set(value) {
            if (value != null) {
                _links["next"] = value
            }
        }
        get() = _links["next"]


    @get:JsonIgnore
    var previous: HalLink?
        set(value) {
            if (value != null) {
                _links["previous"] = value
            }
        }
        get() = _links["previous"]


    @get:JsonIgnore
    var _self: HalLink?
        set(value) {
            if (value != null) {
                _links["self"] = value
            }
        }
        get() = _links["self"]


    init {
        this.next = next
        this.previous = previous
        this._self = _self
    }
}


