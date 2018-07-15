package org.tsdes.advanced.rest.exceptionhandling.constraint

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotNull
import kotlin.reflect.KClass

@NotNull
@Constraint(validatedBy = [(NotZeroValidator::class)])
//tells on what the @ annotation can be used
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)  //specify it should end up in the bytecode and be readable using reflection
@Documented
annotation class NotZero (

    val message: String = "Value must not be zero",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)