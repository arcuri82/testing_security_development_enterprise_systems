package org.tsdes.spring.examples.news

import org.hibernate.validator.constraints.NotBlank
import org.tsdes.spring.examples.news.constraint.Country
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by arcuri82 on 16-Jun-17.
 */
@Entity
class NewsEntity(
                      @get:NotBlank @get:Size(max = 32)
                      var authorId: String,

                      @get:NotBlank @get:Size(max = 1024)
                      var text: String,

                      @get:NotNull
                      var creationTime: ZonedDateTime,

                      @get:Country
                      var country: String,

                      @get:Id @get:GeneratedValue
                      var id: Long? = null
)