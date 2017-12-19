package org.tsdes.spring.examples.news.constraint

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Created by arcuri82 on 16-Jun-17.
 */
class CountryValidator : ConstraintValidator<Country, String> {

    override fun initialize(constraintAnnotation: Country) {
    }

    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        return CountryList.isValidCountry(value)
    }
}