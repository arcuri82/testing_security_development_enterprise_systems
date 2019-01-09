package org.tsdes.intro.jee.ejb.multithreading.jse;

import org.tsdes.intro.jee.ejb.multithreading.Counter;

/**
 * Created by arcuri82 on 09-Jan-19.
 */
public class Example06 implements Counter {

    private int x;

    private final Object lock = new Object();

    @Override
    public void incrementCounter() {

        /*
            If you want to use a synchronized block, it is usually not a good
            practice to synchronized on "this", as the object holding the lock
            will be visible outside of this class, and other threads could put
            their locks on such object instances, possibly leading to deadlocks.
            This can be prevented by locking on an internal private object. So
            only the code in this class can put locks on it.
         */

        synchronized (lock) {
            x = x + 1;
        }
    }

    @Override
    public int getCounter() {
        return x;
    }
}
