package org.tsdes.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class EJB_07_MANDATORYTest extends TestBase{

    @EJB
    private EJB_07_MANDATORY ejb;

    @Test
    public void testWrite(){

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