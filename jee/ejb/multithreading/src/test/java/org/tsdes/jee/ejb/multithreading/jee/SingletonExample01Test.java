package org.tsdes.jee.ejb.multithreading.jee;

import org.tsdes.jee.ejb.multithreading.CounterTestBaseJEE;

public class SingletonExample01Test extends CounterTestBaseJEE {

    @Override
    protected Class<?> getSingletonClass() {
        return SingletonExample01.class;
    }
}