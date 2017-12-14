package org.tsdes.intro.jee.ejb.multithreading;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public abstract class CounterTestBase {

    protected abstract Counter getCounter();

    @Test
    public void doTest(){

        final Counter counter = getCounter();

        final int nThreads = 4;
        final int loops = 100_000;

        List<Thread> threads = new ArrayList<>();

        for(int i=0; i<nThreads; i++){

            Thread t = new Thread(){
                @Override
                public void run(){
                    for(int j=0; j<loops; j++){
                        counter.incrementCounter();
                    }
                }
            };
            t.start();
            threads.add(t);
        }

        //Java 8 stream, could have used just a loop
        threads.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });


        int expected = nThreads * loops;
        assertEquals(expected, counter.getCounter());
    }
}