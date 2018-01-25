package org.tsdes.intro.spring.bean.service.root.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 25-Jan-18.
 */
@RunWith(SpringRunner.class)
//Note: as @SpringBootApplication in a super-package, no need
//to explicitly specify the Application.class here
@SpringBootTest
public class BasePrototypeTest {

    /*
        These two will be different bean instances
     */

    @Autowired
    private BasePrototype first;

    @Autowired
    private BasePrototype second;


    @Test
    public void testPrototype(){

        /*
            Confirm that "first"/"second" are
            different beans
         */
        int a = first.getCounter();
        int b = second.getCounter();

        assertEquals(a, b);

        first.increment();

        int c = first.getCounter();
        assertEquals(a + 1, c);

        int d = second.getCounter();
        assertNotEquals(c, d);
        assertEquals(b, d);

        assertNotEquals(first, second);
    }
}