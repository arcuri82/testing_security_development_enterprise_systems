package org.tsdes.intro.jee.ejb.multithreading;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public abstract class CounterTestBase {

    protected abstract Counter getCounter();

    @Test
    public void doTest(){

        final Counter counter = getCounter();

        final int nThreads = 4;
        final int loops = 100_000;

        List<Thread> threads = new ArrayList<>();

        for(int i=0; i<nThreads; i++){

            Thread t = new Thread(() -> {
                for(int j=0; j<loops; j++){
                    counter.incrementCounter();
                }
            });
            t.start();
            threads.add(t);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });


        int expected = nThreads * loops;
        assertEquals(expected, counter.getCounter());
    }
}