package org.tsdes.jee.ejb.stateless;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserBeanTest {


    @Test
    public void testBeanAsRegularJava(){

        UserBean bean = new UserBean();

        try{
            bean.registerNewUser("a","b","c");
            /*
                This should fail with a NPE, because the bean is not managed
                by the container.
                even if somehow we would get an entity manager (eg through a setter),
                would still fail because not executed inside a transaction
             */
            fail();
        } catch (NullPointerException e){
            //expected
        }
    }


}