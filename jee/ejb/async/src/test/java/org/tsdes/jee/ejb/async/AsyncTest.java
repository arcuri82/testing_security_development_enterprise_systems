package org.tsdes.jee.ejb.async;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

//for testing @Asynchronous we need a real JEE container... an embedded one would not do
@RunWith(Arquillian.class)
public class AsyncTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClasses(A.class);
    }

    @EJB
    private A a;

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
}