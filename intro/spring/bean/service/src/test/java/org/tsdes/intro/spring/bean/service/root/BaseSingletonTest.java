package org.tsdes.intro.spring.bean.service.root;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//This annotation modifies how this test is run in JUnit, in particular start a Spring context.
//Take a moment to appreciate the lack of downloading Wildfly via Maven, and
//no need of manual configurations for Arquillian...
@ExtendWith(SpringExtension.class)
//Tells we are using SpringBoot, and not just Spring. This is important
//for bean discovery based on the @SpringBootApplication class
@SpringBootTest
public class BaseSingletonTest {

    /*
        Annotation for injection in Spring is @Autowired.
        Note: could also use @Inject from JEE CDI, and that would work
        as well (but need to add JEE specs as library).

        By default, a @Service is a "Singleton", so following
        two fields have reference to same variable.
     */

    @Autowired
    private BaseSingleton first;

    @Autowired
    private BaseSingleton second;


    @Test
    public void testSingleton(){

        /*
            Confirm that "first"/"second" are
            the same bean
         */

        int a = first.getCounter();
        int b = second.getCounter();

        assertEquals(a, b);

        first.increment();

        int c = first.getCounter();
        assertEquals(a + 1, c);

        int d = second.getCounter();
        assertEquals(c, d);

        assertEquals(first, second);
    }

    @Test
    public void testConcurrency(){

        /*
            In contrast to EJB @Singleton,
            @Service singletons do NOT have automated
            synchronization. You need to do it explicitly
            if you need it.
         */

        first.setCounter(0);

        final int nThreads = 4;
        final int loops = 100_000_000;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {

            Thread t = new Thread( () -> {
                for(int j=0; j<loops; j++){
                    first.increment();
                }
            });
            t.start();
            threads.add(t);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });

        int expected = nThreads * loops;
        int result = first.getCounter();

        System.out.println("Result: " + result);
        assertNotEquals(expected, result);
        assertTrue(result > loops);
    }
}