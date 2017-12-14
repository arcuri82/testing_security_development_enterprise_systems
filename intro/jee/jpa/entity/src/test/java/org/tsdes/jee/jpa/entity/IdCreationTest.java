package org.tsdes.jee.jpa.entity;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

public class IdCreationTest {

    @Test
    public void testIdPersistence(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DB");//same name as in persistence.xml
        EntityManager em = factory.createEntityManager();//it works as a cache/buffer until we commit a transaction

        User01 user01 = new User01();
        user01.setName("AName");
        user01.setSurname("ASurname");

        // by default, no id, until data committed to the database
        assertNull(user01.getId());

        //committing data to database needs to be inside a transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            /*
                The following is actually executing this SQL statement:

                insert into User01 (name, surname, id) values (?, ?, ?)

             */
            em.persist(user01);

            //there can be several operations on the "cache" EntityManager before we actually commit the transaction
            tx.commit();
        } catch (Exception e){
            //abort the transaction if there was any exception
            tx.rollback();
            fail();//fail the test
        } finally {
            //in any case, make sure to close the opened resources
            em.close();
            factory.close();
        }

        //id should have now be set
        assertNotNull(user01.getId());
        System.out.println("GENERATED ID: "+user01.getId());
    }
}