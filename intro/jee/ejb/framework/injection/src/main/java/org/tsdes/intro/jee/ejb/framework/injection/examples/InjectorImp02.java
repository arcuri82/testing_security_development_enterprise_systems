package org.tsdes.intro.jee.ejb.framework.injection.examples;

import org.tsdes.intro.jee.ejb.framework.injection.Injector;

public class InjectorImp02 implements Injector {

    public <T> T createInstance(Class<T> klass) throws IllegalArgumentException {

        if(klass == null){
            throw new IllegalArgumentException("Null input");
        }

        return null;
    }
}
