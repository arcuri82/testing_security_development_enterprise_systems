package org.tsdes.intro.jee.ejb.stateless;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ejb.EJBException;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class UserBeanInEmbeddedContainerTest {

    protected static EJBContainer ec;
    protected static Context ctx;

    @BeforeEach
    public void initContainer() throws Exception {

        /*
            This will start an embedded container, which we ll save us
            from having to start it as an external process and deploy
            our WAR on it.

            However, embedded containers only provide reduced functionalities,
            see page 231 in Chapter 7 and
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

    protected <T> T getEJB(Class<T> klass){
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

    @AfterEach
    public void closeContainer() throws Exception {
        if (ctx != null)
            ctx.close();
        if (ec != null)
            ec.close();
    }



    @Test
    public void testWithEmbeddedContainer(){

        UserBean bean = getEJB(UserBean.class);

        String userId = "foo";

        assertFalse(bean.isRegistered(userId));

        bean.registerNewUser(userId,"a","b");

        assertTrue(bean.isRegistered(userId));
    }

    @Test
    public void testQuery(){
        UserBean bean = getEJB(UserBean.class);

        bean.registerNewUser("0","a","b");
        bean.registerNewUser("1","a","b");
        bean.registerNewUser("2","a","b");

        long n = bean.getNumberOfUsers();
        assertEquals(3, n);
    }

    @Test
    public void testNullValue(){
        UserBean bean = getEJB(UserBean.class);

        try {
            //In EJB, the @NotNull are checked at runtime by the JEE container
            bean.registerNewUser("0", "a", null);
            fail();
        } catch (EJBException e){
            //expected
        }
    }
}
