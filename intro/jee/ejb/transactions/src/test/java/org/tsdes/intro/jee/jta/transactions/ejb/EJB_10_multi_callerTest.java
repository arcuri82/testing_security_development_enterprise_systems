package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EJB_10_multi_callerTest extends TestBase{

    @EJB
    private EJB_10_multi_caller caller;

    @Test
    public void testNewTransaction(){

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        caller.exe(true, first, second);

        assertTrue(queriesEJB.isInDB(first));
        assertTrue(queriesEJB.isInDB(second));
    }

    @Test
    public void testSameTransaction(){

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        caller.exe(false, first, second);

        //should had rolledback
        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));
    }

}