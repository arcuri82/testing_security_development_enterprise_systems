package org.tsdes.intro.jee.ejb.framework.injection.examples;

import org.tsdes.intro.jee.ejb.framework.injection.Injector;

public class InjectorImp03 implements Injector {


    public <T> T createInstance(Class<T> klass) throws IllegalArgumentException {

        if(klass == null){
            throw new IllegalArgumentException("Null input");
        }

        T t;

        try {
            t = klass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to instantiate object for "+klass.getName()+" : "+e.getMessage());
        }

        return t;
    }
}
