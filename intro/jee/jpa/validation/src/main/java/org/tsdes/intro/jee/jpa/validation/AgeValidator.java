package org.tsdes.intro.jee.jpa.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;


public class AgeValidator implements ConstraintValidator<Age, LocalDate>{

    private int min;

    @Override
    public void initialize(Age constraintAnnotation) {
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

        int years = Period.between(value, LocalDate.now()).getYears();

        return years >= min;
    }
}
