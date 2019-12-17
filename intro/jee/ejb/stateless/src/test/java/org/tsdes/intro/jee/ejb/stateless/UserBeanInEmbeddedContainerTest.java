package org.tsdes.intro.jee.ejb.stateless;



import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(Arquillian.class)
public class UserBeanInEmbeddedContainerTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(UserBean.class, User.class)
                //besides the classes, also need to add resources
                .addAsResource("META-INF/persistence.xml");
    }


    @EJB
    private UserBean bean;

    @Test
    public void testWithEmbeddedContainer(){

        String userId = "foo";

        assertFalse(bean.isRegistered(userId));

        bean.registerNewUser(userId,"a","b");

        assertTrue(bean.isRegistered(userId));
    }

    @Test
    public void testQuery(){

        /*
            As database is not reset, I cannot assume how many users are already in it,
            as that depends on the order in which the tests are executed
         */
        long k = bean.getNumberOfUsers();

        bean.registerNewUser("0","a","b");
        bean.registerNewUser("1","a","b");
        bean.registerNewUser("2","a","b");

        long n = bean.getNumberOfUsers();
        assertEquals(k+3, n);
    }

    @Test
    public void testNullValue(){

        //In EJB, the @NotNull are checked at runtime by the JEE container
        //assertThrows(EJBException.class, () -> bean.registerNewUser("0", "a", null));

        try{
            bean.registerNewUser("0", "a", null);
            fail();
        }catch (EJBException e){
            //expected
        }

    }
}
