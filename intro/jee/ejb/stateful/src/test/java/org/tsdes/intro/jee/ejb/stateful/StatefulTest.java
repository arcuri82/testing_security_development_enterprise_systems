package org.tsdes.intro.jee.ejb.stateful;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.ejb.EJB;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class StatefulTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(A.class, Counter.class, StatefulCounter.class, StatelessCounter.class);
    }


    @EJB
    private A a;

    @Test
    public void testStateful(){

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