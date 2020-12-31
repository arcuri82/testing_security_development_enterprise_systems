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

        /*
            make sure the 2 elements are not in the DB (as DB is cleaned before/after each
            test case, see code in superclass)
         */
        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        /*
            here, we try a write, but rollback it before committing it...
            so nothing should happen in the DB
         */
        ejb.createTwo(true,first,second); //should rollback

        //still not in the DB
        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        //try again, but with no rollback
        ejb.createTwo(false,first,second);

        //now they should had been persisted
        assertTrue(queriesEJB.isInDB(first));
        assertTrue(queriesEJB.isInDB(second));
    }

}