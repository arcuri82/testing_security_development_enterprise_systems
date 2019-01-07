package org.tsdes.intro.jee.ejb.stateless;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class UserBeanTest {


    @Test
    public void testBeanAsRegularJava(){

        UserBean bean = new UserBean();

        /*
           This should fail with a NPE, because the bean is not managed
           by the container.
           Even if somehow we would get an entity manager (eg through a setter),
           would still fail because not executed inside a transaction
         */
        assertThrows(NullPointerException.class, () -> bean.registerNewUser("a","b","c"));
    }

}