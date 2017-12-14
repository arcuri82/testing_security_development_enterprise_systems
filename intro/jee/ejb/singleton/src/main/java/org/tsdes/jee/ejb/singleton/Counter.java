package org.tsdes.jee.ejb.singleton;


import javax.ejb.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class Counter {

    /*
        Access to Singleton classes are automatically synchronized (more details later)
        by the JEE container, but, when dealing multithreaded code, it is always
        good to be on the safe-side and add further synchronization, as
        long as performance is not hit too much...
     */
    private final AtomicInteger counter = new AtomicInteger(0);


    public void increment(){
        /*
            having:
            ----------------
            int counter = 0;
            counter = counter + 1
            ----------------
            is unsafe, because by the time you execute "counter + 1"
            another thread might have modified it (eg after few nanoseconds),
            and so "counter = ..." would use a stale value
         */
        counter.incrementAndGet();
    }

    public int get(){
        return counter.get();
    }
}
