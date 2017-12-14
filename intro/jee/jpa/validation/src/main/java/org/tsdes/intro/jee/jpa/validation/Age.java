package org.tsdes.intro.jee.jpa.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Past;
import java.lang.annotation.*;

@Past //further constraint
@Constraint(validatedBy = AgeValidator.class)
@Target({  //tells on what the @ annotation can be used
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE}
)
@Retention(RetentionPolicy.RUNTIME) //specify it should end up in the bytecode and be readable using reflection
@Documented //should be part of the JavaDoc of where it is applied to
public @interface Age {

    //these 3 are default needed methods
    String message() default "Too young";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    //we want to have a generic age constraint, where the "min" is a parameter
    int min() default 0;
}
