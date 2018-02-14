package org.tsdes.intro.spring.testing.mocking;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

/**
 * Created by arcuri82 on 14-Feb-18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = NONE)
public class ServiceASpringTest {

    @Autowired
    private ServiceA serviceA;

    @Autowired
    private ServiceB serviceB;


    @Test
    public void testOK(){

        String res = serviceA.check(-1);
        assertEquals("NOT OK", res);

        long id = serviceB.createEntry(true);
        res = serviceA.check(id);
        assertEquals("OK", res);
    }
}