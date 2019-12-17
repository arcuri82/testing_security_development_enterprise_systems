package org.tsdes.intro.jee.ejb.callback;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CallbackTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(A.class, B.class);
    }

    @EJB
    private A a;

    @EJB
    private B b;

    @Test
    public void testPostConstruct(){

        A directInstance = new A();
        assertNull(directInstance.getValue());

        String res = a.getValue();
        assertNotNull(res);

        assertEquals(b.getValue(), res);
    }
}