package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EJB_01_REQUIRED_Test extends TestBase{

    @EJB
    private EJB_01_REQUIRED ejb;

    @Test
    public void test(){

        /*
            The tests are not run in a transaction
         */

        String name = "a name";

        /*
            calls on proxy beans will start a transaction, and commit it
            at the end of the call.
         */
        assertFalse(queriesEJB.isInDB(name));

        /*
            transaction should complete normally.
            As the method is marked REQUIRED, and there is no active transaction
            before this method is called, a new transaction will be started
         */
        ejb.createFoo(name);

        assertTrue(queriesEJB.isInDB(name));
    }
}