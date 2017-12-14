package org.tsdes.jee.ejb.multithreading.jee;

import org.tsdes.jee.ejb.multithreading.CounterTestBaseJEE;

public class SingletonExample03TestNot extends CounterTestBaseJEE {

    @Override
    protected Class<?> getSingletonClass() {
        return SingletonExample03.class;
    }
}