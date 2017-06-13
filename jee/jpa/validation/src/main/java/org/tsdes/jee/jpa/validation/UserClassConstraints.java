package org.tsdes.jee.jpa.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = UserClassConstraintsValidator.class)
@Target({
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE}
)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserClassConstraints {

    String message() default "Invalid constraints in User's state";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
