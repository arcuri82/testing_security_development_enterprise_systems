package org.tsdes.intro.jee.ejb.callback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CallbackTest {

    protected static EJBContainer ec;
    protected static Context ctx;

    @Before
    public void initContainer() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put(EJBContainer.MODULES, new File("target/classes"));
        ec = EJBContainer.createEJBContainer(properties);
        ctx = ec.getContext();
    }

    protected <T> T getEJB(Class<T> klass){
        try {
            return (T) ctx.lookup("java:global/classes/"+klass.getSimpleName()+"!"+klass.getName());
        } catch (NamingException e) {
            return null;
        }
    }

    @After
    public void closeContainer() throws Exception {
        if (ctx != null)
            ctx.close();
        if (ec != null)
            ec.close();
    }


    @Test
    public void testPostConstruct(){

        A directInstance = new A();
        assertNull(directInstance.getValue());

        A a = getEJB(A.class);
        String res = a.getValue();

        assertNotNull(res);

        B b = getEJB(B.class);
        assertEquals(b.getValue(), res);
    }
}