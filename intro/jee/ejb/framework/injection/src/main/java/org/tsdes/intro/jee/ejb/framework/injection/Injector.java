package org.tsdes.intro.jee.ejb.framework.injection;

/**
 * Service responsible to create new object instances where all injectable
 * fields are injected
 */
public interface Injector {

    /**
     * Create a new instance for the input class, where all fields marked with AnnotatedForInjection are
     * injected.
     *
     * @return a non-null instance of class T
     * @throws IllegalArgumentException if input is invalid (eg null), or if any injectable object does not have a default,
     *  zero-parameter constructor
     */
    <T> T createInstance(Class<T> klass) throws IllegalArgumentException;
}
