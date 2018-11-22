package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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