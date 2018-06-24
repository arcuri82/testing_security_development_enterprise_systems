package org.tsdes.advanced.rest.newsrestv2.dto

import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime
import org.tsdes.advanced.examplenews.constraint.Country
import java.lang.Deprecated

/**
 * Created by arcuri82 on 12-Jul-17.
 */
data class NewsDto(

        /*
                This is the easiest way to handle changing the name of a
                property, eg from "id" to "newsId":
                just add a new property, and mark "Deprecated" the old.
                Then sending data, just send both.
                When looking at which id to use, first look at "newsId" and,
                if missing, only then look at "id".

                Note: to make things more sophisticated, we could avoid
                sending "id" when a client ask for the "new" format.
                But to do that, we need to modify how Jackson do marshalling.

                If there are many changes, another option is to create a new DTO
                for the new version
          */

        @ApiModelProperty("The id of the news")
        var newsId: String? = null,

        @ApiModelProperty("The id of the author that wrote/created this news")
        var authorId: String? = null,

        @ApiModelProperty("The text of the news")
        var text: String? = null,

        @ApiModelProperty("The country this news is related to")
        @get:Country
        var country: String? = null,

        @ApiModelProperty("When the news was first created/published")
        var creationTime: ZonedDateTime? = null
) {


    @ApiModelProperty("Deprecated. Use newsId instead")
    @Deprecated
    var id: String? = null

}