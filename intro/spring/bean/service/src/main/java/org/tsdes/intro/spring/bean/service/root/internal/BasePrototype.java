package org.tsdes.intro.spring.bean.service.root.internal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by arcuri82 on 25-Jan-18.
 */
@Service
//change scope from default Singleton
@Scope("prototype")
public class BasePrototype {

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
