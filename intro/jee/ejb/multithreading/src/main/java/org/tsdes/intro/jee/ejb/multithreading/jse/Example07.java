package org.tsdes.intro.jee.ejb.multithreading.jse;

import org.tsdes.intro.jee.ejb.multithreading.Counter;

import java.util.concurrent.atomic.AtomicInteger;

public class Example07 implements Counter {

    /*
        The java.util.concurrent package provide several APIs to deal
        with concurrency.
        For example, an AtomicInteger is an integer in which we can do
        read/write operations atomically, instead of requiring to do
        explicit synchronized blocks. The result is the same, so you
        can think about it like useful syntactic sugar.
     */
    private final AtomicInteger x = new AtomicInteger(0);

    @Override
    public void incrementCounter() {
        x.incrementAndGet();
    }

    @Override
    public int getCounter() {
        return x.get();
    }
}