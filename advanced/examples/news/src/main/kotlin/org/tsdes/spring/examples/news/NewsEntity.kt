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
 * In Spring we can use JPA, and so have @Entity objects to model our database.
 *
 * Note how in Kotlin we can avoid most of the boilerplate, like getters/setters.
 *
 * One negative side though is the use of @get: to annotate the properties, which is
 * needed to specify that we want to annotate the "get" method generated for such property.
 *
 * If for example you use @NotBlank instead of @get:NotBlank, such annotation would
 * be applied on the constructor parameter, not on the field :(
 *
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

                        /*
                            Note how we need to explicitly state that id can be null (eg when entity
                            is not in sync with database).
                            The "= null" is used to provide a default value if caller does not
                            provide one.
                         */
)