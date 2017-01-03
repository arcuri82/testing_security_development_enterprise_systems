package org.tsdes.jee.ejb.framework.injection.data;

import org.tsdes.jee.ejb.framework.injection.AnnotatedForInjection;

public class BasicInjectionClass {

    private EmptyClass nonInjectedEmptyClass;

    @AnnotatedForInjection
    private EmptyClass injectedEmptyClass;


    public EmptyClass getNonInjectedEmptyClass() {
        return nonInjectedEmptyClass;
    }

    public EmptyClass getInjectedEmptyClass() {
        return injectedEmptyClass;
    }
}
