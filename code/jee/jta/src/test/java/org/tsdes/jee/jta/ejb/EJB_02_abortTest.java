package org.tsdes.jee.jta.ejb;

import org.junit.Test;

import javax.ejb.EJBException;

import static org.junit.Assert.*;

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