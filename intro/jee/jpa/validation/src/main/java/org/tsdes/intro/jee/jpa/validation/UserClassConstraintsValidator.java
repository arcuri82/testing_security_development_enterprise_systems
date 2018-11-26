package org.tsdes.intro.jee.jpa.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Here we want to represent constraints involving more than one field at a time.
 * For example, we want to state that a registration cannot happen before someone
 * is even born: this requires comparing two (or more) fields, eg dateOfBirth and
 * dateOfRegistration in this case.
 */
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
