package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EJB_03_rollbackTest extends TestBase{

    @EJB
    private EJB_03_rollback ejb;

    @Test
    public void test(){

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwo(true,first,second); //should rollback

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        //try again, but with no rollback
        ejb.createTwo(false,first,second);

        //now they should had been persisted
        assertTrue(queriesEJB.isInDB(first));
        assertTrue(queriesEJB.isInDB(second));
    }

}