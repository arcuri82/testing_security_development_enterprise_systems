package org.tsdes.intro.spring.testing.mocking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

/**
 * Created by arcuri82 on 14-Feb-18.
 */
@ExtendWith(SpringExtension.class)
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