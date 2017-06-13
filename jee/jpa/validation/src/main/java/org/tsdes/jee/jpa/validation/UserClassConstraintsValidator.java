package org.tsdes.jee.jpa.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserClassConstraintsValidator implements ConstraintValidator<UserClassConstraints, User> {


    @Override
    public void initialize(UserClassConstraints constraintAnnotation) {
        //nothing to do here
    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {

        if(value.getDateOfBirth() == null || value.getDateOfRegistration() == null){
            return false;
        }

        //can't be registered before having being born
        return value.getDateOfRegistration().compareTo(value.getDateOfBirth()) > 0;
    }
}
