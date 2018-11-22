package org.tsdes.intro.jee.jpa.entity;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class IdHandlingTest {

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
    public void testNullId() {

        User02 user02 = new User02();

        boolean persisted = persistInATransaction(user02);
        assertFalse(persisted); //the Id is null, and does not get generated. we have to provide it

        user02.setId(1L);

        persisted = persistInATransaction(user02);
        assertTrue(persisted); //this should work
    }


    @Test
    public void testAnotherObjectWithSameId() {
        User02 user02 = new User02();
        user02.setId(1L);

        boolean persisted = persistInATransaction(user02);
        assertTrue(persisted);

        User02 another = new User02();
        another.setId(user02.getId()); // two objects with same id

        persisted = persistInATransaction(another);
        assertFalse(persisted); // two different objects with the same ID
    }


    @Test
    public void testPersistTwice() {
        User02 user02 = new User02();
        user02.setId(1L);

        boolean persisted = persistInATransaction(user02);
        assertTrue(persisted);

        persisted = persistInATransaction(user02);
        assertTrue(persisted); //same object is OK
    }

    @Test
    public void testAnotherObjectWithSameIdButInDifferentSession() {
        User02 user02 = new User02();
        user02.setId(1L);

        boolean persisted = persistInATransaction(user02);
        assertTrue(persisted);

        em.close();
        em = factory.createEntityManager();

        User02 another = new User02();
        another.setId(user02.getId()); // two objects with same id

        persisted = persistInATransaction(another);
        assertFalse(persisted); //this still fails, because Id should be unique
    }

    @Test
    public void testPersistTwiceInDifferentSession() {
        User02 user02 = new User02();
        user02.setId(1L);

        boolean persisted = persistInATransaction(user02);
        assertTrue(persisted);

        em.close();
        em = factory.createEntityManager();

        persisted = persistInATransaction(user02);
        assertFalse(persisted);
        /*
            Even if same object, this fails, because transaction done in two different EntityManager,
         */
    }


    @Test
    public void testPersistTwiceInNewFactory() {
        User02 user02 = new User02();
        user02.setId(1L);

        boolean persisted = persistInATransaction(user02);
        assertTrue(persisted);

        em.close();
        factory.close();
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();

        persisted = persistInATransaction(user02);
        assertTrue(persisted);
        /*
            Here we managed to commit, because createEntityManagerFactory does
            create a new database, as we used "create-drop" in the persisten.xml file
         */
    }
}
