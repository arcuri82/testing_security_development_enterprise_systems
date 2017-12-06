package org.tsdes.jee.exercises.quizgame.backend.entity;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by arcuri82 on 06-Dec-17.
 */
public class EntityTestBase {


    private EntityManagerFactory factory;
    protected EntityManager em;


    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("LOCAL");
        em = factory.createEntityManager();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    protected boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for (Object o : obj) {
                em.persist(o);
            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }
}
