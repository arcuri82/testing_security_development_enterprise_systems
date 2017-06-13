package org.tsdes.jee.ejb.framework.injection;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used to specified if a field should be injected
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface AnnotatedForInjection {
}
