package org.tsdes.intro.spring.bean.service.root;

import org.springframework.stereotype.Service;

/**
 * By default, a @Service is similar to a @Singleton in EJB,
 * but WITHOUT automated synchronization on its methods
 *
 * Created by arcuri82 on 25-Jan-18.
 */
@Service
public class BaseSingleton {

    private int counter;

    public void increment(){
        int x = getCounter();
        setCounter(x + 1);
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
