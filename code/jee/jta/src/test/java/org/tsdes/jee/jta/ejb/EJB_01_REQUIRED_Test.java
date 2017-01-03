package org.tsdes.jee.jta.ejb;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EJB_01_REQUIRED_Test extends TestBase{

    @Test
    public void test(){

        EJB_01_REQUIRED ejb = getEJB(EJB_01_REQUIRED.class);

        String name = "a name";

        assertFalse(queriesEJB.isInDB(name));

        //transaction should complete normally
        ejb.createFoo(name);

        assertTrue(queriesEJB.isInDB(name));
    }
}