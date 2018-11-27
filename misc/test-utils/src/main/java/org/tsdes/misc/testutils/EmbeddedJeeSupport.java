package org.tsdes.misc.testutils;



import javax.naming.Context;
import javax.naming.NamingException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;


/**
 * Created by arcuri82 on 27-Nov-18.
 */
public class EmbeddedJeeSupport {


    protected EJBContainer ec;
    protected Context ctx;

    public void initContainer() {

        /*
            This will start an embedded container, which we ll save us
            from having to start it as an external process and deploy
            our WAR on it.

            However, embedded containers only provide reduced functionalities,
            http://arquillian.org/blog/2012/04/13/the-danger-of-embedded-containers/

            In generate, better to avoid the embedded containers,
            although I will use them in some simple examples just to
            simplify the execution of the tests
         */

        Map<String, Object> properties = new HashMap<>();
        properties.put(EJBContainer.MODULES, new File("target/classes"));
        ec = EJBContainer.createEJBContainer(properties);
        ctx = ec.getContext();
    }

    public <T> T getEJB(Class<T> klass){
        try {
            /*
                Need to use JNDI to look for the EJB by using a string...
                Quite awful indeed...
                Plus, the string prefix might vary based on the actual JEE container...
             */
            return (T) ctx.lookup("java:global/classes/"+klass.getSimpleName()+"!"+klass.getName());
        } catch (NamingException e) {
            return null;
        }
    }

    public <T extends I, I> T getEJB(Class<T> klass, Class<I> theInterface){
        try {
            /*
                Need to use JNDI to look for the EJB by using a string...
                Quite awful indeed...
                Plus, the string prefix might vary based on the actual JEE container...
             */
            return (T) ctx.lookup("java:global/classes/"+klass.getSimpleName()+"!"+theInterface.getName());
        } catch (NamingException e) {
            return null;
        }
    }

    public void closeContainer() throws Exception {
        if (ctx != null)
            ctx.close();
        if (ec != null)
            ec.close();
    }
}
