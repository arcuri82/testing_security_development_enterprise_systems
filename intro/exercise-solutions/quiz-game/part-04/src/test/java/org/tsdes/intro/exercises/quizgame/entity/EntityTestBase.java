package org.tsdes.intro.exercises.quizgame.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public abstract class EntityTestBase {

    private EntityManagerFactory factory;
    protected EntityManager em;

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

    protected boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(Object o : obj) {
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
