package org.tsdes.advanced.rest.exceptionhandling.constraint

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext


class NotZeroValidator : ConstraintValidator<NotZero, Int> {

    override fun initialize(constraintAnnotation: NotZero) {
    }

    override fun isValid(value: Int, context: ConstraintValidatorContext): Boolean {
        return value != 0
    }
}