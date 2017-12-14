package org.tsdes.intro.jee.ejb.callback;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;

@Stateful
public class A {

    @EJB
    private B b;

    private String valueOfB;

    public A(){
        /*
            I cannot call any method of 'b' here, as the JEE container has
            not done the dependency injection yet.

            Note: "assert" checking are off by default, unless you pass "-ea" (enable assertions) to
            the JVM options.
         */
        assert b == null;
    }

    @PostConstruct //this executed after the JEE call the constructor of this EJB
    public void init(){
        //here we know that 'b' has been injected
        valueOfB = b.getValue();
    }

    public String getValue(){
        return valueOfB;
    }

    /*
         There is also:
         - @PreDestroy: called before the JEE container delete this instance
         - @PrePassivate: only for @Stateful, called before JEE decides to save instance to disk
         - @PostActivate: only for @Stateful, called once JEE resumes instance from disk
     */
}
