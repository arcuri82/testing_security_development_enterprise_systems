package org.tsdes.intro.jee.jta.transactions.ejb;

import org.junit.Test;

import static org.junit.Assert.*;

public class EJB_03_rollbackTest extends TestBase{

    @Test
    public void test(){

        EJB_03_rollback ejb = getEJB(EJB_03_rollback.class);

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