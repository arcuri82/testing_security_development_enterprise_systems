package org.tsdes.jee.ejb.singleton;


import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class B {

    //dependency injection
    @EJB
    private Counter counter;

    public void incrementCounter(){
        counter.increment();
    }
}
