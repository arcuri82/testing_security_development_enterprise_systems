package org.tsdes.intro.jee.ejb.framework.proxy.se;

import org.junit.jupiter.api.Test;
import org.tsdes.intro.jee.ejb.framework.proxy.se.se.ObjectFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ObjectFactoryTest {

    @Test
    public void testProxy() {

        ObjectFactory factory = new ObjectFactory();
        //no call so far
        assertEquals(0, factory.getTotalInvocationCount());

        A a = new AImp();
        assertEquals("X", a.methodX());
        assertEquals("Y", a.methodY());

        //still 0, as A is not proxied
        assertEquals(0, factory.getTotalInvocationCount());

        A proxy = factory.createInstance(A.class, AImp.class);
        assertNotNull(proxy);

        //still the behavior is the same
        assertEquals("X", proxy.methodX());
        assertEquals("Y", proxy.methodY());

        //however, now the calls have been intercepted
        assertEquals(2, factory.getTotalInvocationCount());
    }

}