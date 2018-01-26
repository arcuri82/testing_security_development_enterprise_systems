package org.tsdes.intro.spring.bean.configuration.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyServiceTest {

    @Autowired
    private MyServiceImpA impA;

    @Autowired
    private MyServiceImpB impB;

    //to resolve ambiguity, variable name equal to the bean name
    @Autowired
    private MyService myServiceImpA;

    //another option to resolver ambiguity is to use @Qualifier
    @Autowired
    @Qualifier("myServiceImpB")
    private MyService foo;

    /*
        Note the use of "myServiceImpB" instead of "MyServiceImpB", ie
        first letter is lower-case.
        When you create a bean via class annotations (eg @Service), the name
        of the bean will be the name of the class, but with first letter in
        lower case.
     */


    @Test
    public void testWiring(){
        /*
            note: these checks are actually redundant...
            because if they were not injected, Spring would have
            thrown exception during initialization, and this test
            would not be even started
         */
        assertNotNull(impA);
        assertNotNull(impB);
        assertNotNull(myServiceImpA);
        assertNotNull(foo);

        //recall they are singleton instances
        assertEquals(impA, myServiceImpA);
        assertEquals(impB, foo);
    }
}