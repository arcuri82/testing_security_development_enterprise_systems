package org.tsdes.jee.ejb.multithreading.jee;

import org.tsdes.jee.ejb.multithreading.CounterTestBaseJEE;

public class SingletonExample02TestNot extends CounterTestBaseJEE {

    @Override
    protected Class<?> getSingletonClass() {
        return SingletonExample02.class;
    }
}