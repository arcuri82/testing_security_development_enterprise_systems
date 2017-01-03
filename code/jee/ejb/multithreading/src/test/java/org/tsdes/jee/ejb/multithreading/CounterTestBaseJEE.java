package org.tsdes.jee.ejb.multithreading;

import org.junit.After;
import org.junit.Before;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class CounterTestBaseJEE extends CounterTestBase {

    private EJBContainer ec;
    private Context ctx;

    @Before
    public void initContainer() throws Exception {

        /*
            Using an embedded JEE container...
            recall, this is done just to simplify those examples, but
            you will have to use Arquillian when testing an actual application
         */

        Map<String, Object> properties = new HashMap<>();
        properties.put(EJBContainer.MODULES, new File("target/classes"));
        ec = EJBContainer.createEJBContainer(properties);
        ctx = ec.getContext();
    }

    @After
    public void closeContainer() throws Exception {
        if (ctx != null)
            ctx.close();
        if (ec != null)
            ec.close();
    }

    @Override
    public Counter getCounter(){
        String name = getSingletonClass().getSimpleName();
        try {
            return (Counter) ctx.lookup("java:global/classes/"+name+"!"+Counter.class.getName());
        } catch (NamingException e) {
            return null;
        }
    }

    protected abstract Class<?> getSingletonClass();
}
