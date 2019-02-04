package org.tsdes.intro.spring.bean.async;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AsyncServiceTest {


    @Autowired
    private AsyncService a;

    @Test
    public void testAsync() throws InterruptedException {

        int x = 0;
        a.setX(x);

        assertEquals(0 , a.getX());

        //if Async works as expected, this invocation should return immediately
        a.compute();

        //as it should take at least 2 seconds, now shouldn't have been changed yet
        assertEquals(0 , a.getX());

        Thread.sleep(4_000); //just to be on safe side, let's wait 4 seconds

        //by now, it should have been changed
        assertNotEquals(0 , a.getX());
    }


    @Test
    public void testAsyncResult() throws Exception {

        Future<String> future = a.asyncResult();

        //this is blocking
        String result = future.get();

        assertEquals("foo", result);
    }
}