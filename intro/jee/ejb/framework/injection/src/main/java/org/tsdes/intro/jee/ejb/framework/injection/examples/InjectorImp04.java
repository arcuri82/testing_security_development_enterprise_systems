package org.tsdes.intro.jee.ejb.framework.injection.examples;

import org.tsdes.intro.jee.ejb.framework.injection.AnnotatedForInjection;
import org.tsdes.intro.jee.ejb.framework.injection.Injector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class InjectorImp04 implements Injector {


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


        for(Field field : klass.getDeclaredFields()){

            Annotation annotation = field.getAnnotation(AnnotatedForInjection.class);
            if(annotation==null){
                //skip, as field was not marked for injection
                continue;
            }

            try {
                Class<?> typeToInject = field.getType();
                Object objectToInject = typeToInject.getDeclaredConstructor().newInstance();
                field.setAccessible(true); //needed, otherwise fails on private fields
                field.set(t, objectToInject);
            } catch (Exception e){
                throw new IllegalArgumentException("Not possible to inject "+field.getName()+" due to: "+e.getMessage());
            }
        }

        return t;
    }
}
