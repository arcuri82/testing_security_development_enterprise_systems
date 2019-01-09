package org.tsdes.intro.jee.ejb.multithreading.jse;

import org.tsdes.intro.jee.ejb.multithreading.Counter;

public class Example02 implements Counter {

    /**
     * Note the "volatile" keywords here to force reading/writing to RAM
     * and not local thread cache
     */
    private volatile int x;

    @Override
    public void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}

