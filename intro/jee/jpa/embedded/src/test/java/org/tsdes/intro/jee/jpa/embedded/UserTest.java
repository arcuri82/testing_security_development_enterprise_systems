package org.tsdes.intro.jee.jpa.embedded;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserTest {

    private EntityManagerFactory factory;
    private EntityManager em;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        factory.close();
    }

    private boolean persistInATransaction(Object obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            em.persist(obj);
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }


    @Test
    public void testUser(){

        UserId id = new UserId();
        id.setName("aName");
        id.setSurname("aSurname");

        Address address = new Address();
        address.setCity("Oslo");
        address.setCountry("Norway");
        address.setPostcode(123L);

        User user = new User();
        user.setId(id);
        user.setAddress(address);


        /*
            This one does execute:

            INSERT INTO User (city, country, postcode, name, surname) values (?, ?, ?, ?, ?)
         */
        boolean persisted = persistInATransaction(user);
        assertTrue(persisted);
    }
}