package org.tsdes.jee.ejb.multithreading.jee;

import org.tsdes.jee.ejb.multithreading.Counter;

import javax.ejb.Singleton;

@Singleton
public class SingletonExample01 implements Counter {

    private int x;

    @Override
    public void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}
