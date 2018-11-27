package org.tsdes.intro.jee.ejb.stateless;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tsdes.misc.testutils.EmbeddedJeeSupport;
import javax.ejb.EJBException;

import static org.junit.jupiter.api.Assertions.*;


public class UserBeanInEmbeddedContainerTest {

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
    public void testWithEmbeddedContainer(){

        UserBean bean = container.getEJB(UserBean.class);

        String userId = "foo";

        assertFalse(bean.isRegistered(userId));

        bean.registerNewUser(userId,"a","b");

        assertTrue(bean.isRegistered(userId));
    }

    @Test
    public void testQuery(){
        UserBean bean = container.getEJB(UserBean.class);

        bean.registerNewUser("0","a","b");
        bean.registerNewUser("1","a","b");
        bean.registerNewUser("2","a","b");

        long n = bean.getNumberOfUsers();
        assertEquals(3, n);
    }

    @Test
    public void testNullValue(){
        UserBean bean = container.getEJB(UserBean.class);

        try {
            //In EJB, the @NotNull are checked at runtime by the JEE container
            bean.registerNewUser("0", "a", null);
            fail();
        } catch (EJBException e){
            //expected
        }
    }
}
