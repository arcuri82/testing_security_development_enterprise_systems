package org.tsdes.intro.jee.ejb.multithreading.jee;

import org.tsdes.intro.jee.ejb.multithreading.Counter;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class SingletonExample03 implements Counter {

    private int x;

    /*
        yes, we are going to wait until lock is released, ie
        another thread executing this method in parallel.
        However, we are not going to wait forever,
        and we can specify a timeout
     */
    @AccessTimeout(value = 2 , unit = TimeUnit.MILLISECONDS)
    @Override
    public void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}
