package org.tsdes.spring.examples.news.constraint

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotNull
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

/**
 * Created by arcuri82 on 16-Jun-17.
 */
@NotNull
@Constraint(validatedBy = arrayOf(CountryValidator::class))
//tells on what the @ annotation can be used
@Target(FIELD, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, VALUE_PARAMETER, ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)  //specify it should end up in the bytecode and be readable using reflection
@Documented
annotation class Country(
        val message: String = "Invalid country",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)