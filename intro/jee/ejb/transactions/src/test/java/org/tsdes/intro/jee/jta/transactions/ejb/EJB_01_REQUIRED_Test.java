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

        String name = "a name";

        assertFalse(queriesEJB.isInDB(name));

        //transaction should complete normally
        ejb.createFoo(name);

        assertTrue(queriesEJB.isInDB(name));
    }
}