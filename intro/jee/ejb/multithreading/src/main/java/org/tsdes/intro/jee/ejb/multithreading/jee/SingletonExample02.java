package org.tsdes.intro.jee.ejb.multithreading.jee;

import org.tsdes.intro.jee.ejb.multithreading.Counter;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Lock(LockType.WRITE)
@Singleton
public class SingletonExample02 implements Counter {

    private int x;

    @Lock(LockType.READ)
    @Override
    public void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}
