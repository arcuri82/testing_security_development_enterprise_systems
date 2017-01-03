package org.tsdes.jee.ejb.stateful;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class A {

    /*
        Recall: these injected EJB are not actual instances, but proxies.
        However, the @Stateful is guaranteed to point to the same instance,
        whereas for @Stateless it might always different at each single
        invocation
     */

    @EJB
    private StatefulCounter statefulCounter;
    @EJB
    private StatelessCounter statelessCounter;


    public void increment(){
        statefulCounter.increment();
        statelessCounter.increment();
    }

    public int getStateful(){
        return statefulCounter.get();
    }

    public int getStateless(){
        return statelessCounter.get();
    }
}
