package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EJB_10_multi_callerTest extends TestBase{


    @Test
    public void testNewTransaction(){
        EJB_10_multi_caller caller = getEJB(EJB_10_multi_caller.class);

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
        EJB_10_multi_caller caller = getEJB(EJB_10_multi_caller.class);

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