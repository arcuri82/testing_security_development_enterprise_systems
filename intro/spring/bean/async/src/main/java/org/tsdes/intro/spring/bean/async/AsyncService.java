package org.tsdes.intro.spring.bean.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;


@Service
public class AsyncService {

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

        Note: need to configure @EnableAsync
     */
    @Async
    public void compute(){

        try {
            //simulate some computation by sleeping 2 seconds
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
        }

        //just to check that this method is complete, change x
        x = 42;
    }

    /*
        If we care of the result, we can get back a Future object on
        which we can wait on until the async method is completed.
     */
    @Async
    public Future<String> asyncResult(){

        try {
            //simulate some computation by sleeping 2 seconds
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
        }

        return new AsyncResult<>("foo");
    }
}
