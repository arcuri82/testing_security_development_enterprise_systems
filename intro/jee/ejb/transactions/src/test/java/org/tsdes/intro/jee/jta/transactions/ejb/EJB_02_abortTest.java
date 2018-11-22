package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.jupiter.api.Test;

import javax.ejb.EJBException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;


public class EJB_02_abortTest extends TestBase{

    @Test
    public void test(){

        EJB_02_abort ejb = getEJB(EJB_02_abort.class);

        String name = "a name";

        assertFalse(queriesEJB.isInDB(name));

        try {
            ejb.createTwoCopies(name); //should fail
            fail();
        } catch (EJBException e){
            //expected
        }

        assertFalse(queriesEJB.isInDB(name));
    }

}