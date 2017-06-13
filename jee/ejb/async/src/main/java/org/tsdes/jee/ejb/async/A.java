package org.tsdes.jee.ejb.async;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;

@Singleton
public class A {

    private int x;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    /*
        useful when we do an expensive operation and we do not care
        to return anything to the caller of the method.
        This is achieved by executing it in a separated thread.
     */
    @Asynchronous
    public void compute(){

        try {
            //simulate some computation by sleeping 2 seconds
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
        }

        //just to check that this method is complete, change x
        x = 42;
    }
}
