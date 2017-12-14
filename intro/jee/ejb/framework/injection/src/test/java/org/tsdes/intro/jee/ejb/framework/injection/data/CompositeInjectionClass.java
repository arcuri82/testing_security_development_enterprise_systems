package org.tsdes.intro.jee.ejb.framework.injection.data;

import org.tsdes.intro.jee.ejb.framework.injection.AnnotatedForInjection;

public class CompositeInjectionClass {

    @AnnotatedForInjection
    private BasicInjectionClass basicInjectionClass;


    public BasicInjectionClass getBasicInjectionClass() {
        return basicInjectionClass;
    }
}
