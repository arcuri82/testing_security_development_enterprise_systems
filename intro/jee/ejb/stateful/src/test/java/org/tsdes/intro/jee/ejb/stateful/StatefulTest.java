package org.tsdes.intro.jee.ejb.stateful;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tsdes.misc.testutils.EmbeddedJeeSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatefulTest {

    private static EmbeddedJeeSupport container = new EmbeddedJeeSupport();

    @BeforeEach
    public void initContainer()  {
        container.initContainer();
    }

    @AfterEach
    public void closeContainer() throws Exception {
        container.closeContainer();
    }


    @Test
    public void testStateful(){

        A a = container.getEJB(A.class);

        a.increment();
        a.increment();
        a.increment();
        int expected = 3;

        /*
            here we know that the reference to StatefulCounter is always the same.
            Need to be careful about Stateful JEE... if you have one per user,
            and your website gets hit by 1M users, you ll get 1M EJB objects in
            memory... to avoid this, the JEE container might save them to disk
            and only keep a fixed small number in memory at the same time
         */
        assertEquals(expected, a.getStateful());

        /*
            Can't say ANYTHING about the stateless... could be 3, could be 0, could
            be whatever... it all depends on what the JEE container decides to use
            in the proxy...
         */
        System.out.println("Stateless: " + a.getStateless());
    }
}