package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class EJB_07_MANDATORYTest extends TestBase{


    @Test
    public void testWrite(){

        EJB_07_MANDATORY ejb = getEJB(EJB_07_MANDATORY.class);

        String name = "bar";

        try{
            ejb.createFooMandatory(name);
            fail();
        } catch (Exception e){
            //expected, as cannot call a method with mandatory transaction from a non-transaction code
        }

        try{
            ejb.createFooSupports(name);
            fail();
        } catch (Exception e){
            //even a "supports" should fail, as trying a write operation outside of a transaction
        }
    }


    @Test
    public void testRead(){

        EJB_07_MANDATORY ejb = getEJB(EJB_07_MANDATORY.class);

        String name = "bar";

        ejb.createFooRequired(name);

        try{
            ejb.isPresentMandatory(name);
            fail();
        } catch (Exception e){
            //expected, as cannot call a method with mandatory transaction from a non-transaction code
        }

        //a "read" operation does not need a transaction, so it should work outside of a transaction
        boolean found = ejb.isPresentSupports(name);
        assertTrue(found);
    }
}