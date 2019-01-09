package org.tsdes.intro.jee.ejb.multithreading.jse;

import org.tsdes.intro.jee.ejb.multithreading.Counter;

public class Example05 implements Counter {

    private volatile int x;

    /*
        Synchronized can be put directly on methods, which is equivalent
        to a "synchronized(this)" command
     */

    @Override
    public synchronized void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}