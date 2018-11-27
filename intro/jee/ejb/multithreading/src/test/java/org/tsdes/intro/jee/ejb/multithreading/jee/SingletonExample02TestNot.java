package org.tsdes.intro.jee.ejb.multithreading.jee;

import org.tsdes.intro.jee.ejb.multithreading.Counter;
import org.tsdes.intro.jee.ejb.multithreading.CounterTestBaseJEE;

public class SingletonExample02TestNot extends CounterTestBaseJEE {

    @Override
    protected Class<? extends Counter> getSingletonClass() {
        return SingletonExample02.class;
    }
}