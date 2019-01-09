package org.tsdes.intro.jee.ejb.multithreading.jse;

import org.tsdes.intro.jee.ejb.multithreading.Counter;

public class Example03 implements Counter {


    private volatile int x;

    @Override
    public void incrementCounter() {
        /*
            We can put a LOCK on an object ("this" in this case).
            No other thread can execute such block (or any block what was synchronized on
            'this') until the lock is released.
            Other threads trying to execute it will be put on hold.
         */
        synchronized (this) {
            x = x + 1;
        }
    }

    @Override
    public int getCounter() {
        return x;
    }
}
