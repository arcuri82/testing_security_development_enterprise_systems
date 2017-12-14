package org.tsdes.intro.jee.jpa.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;


public class AgeValidator implements ConstraintValidator<Age,Date>{

    private int min;

    @Override
    public void initialize(Age constraintAnnotation) {
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {

        Date now = new Date();

        int years = Period.between(
                value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .getYears();

        return years >= min;
    }
}
