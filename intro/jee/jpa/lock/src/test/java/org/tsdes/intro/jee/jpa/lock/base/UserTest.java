package org.tsdes.intro.jee.jpa.lock.base;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tsdes.intro.jee.jpa.lock.TransactionExecutor;

import javax.persistence.*;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    private EntityManagerFactory factory;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @AfterEach
    public void tearDown() {
        factory.close();
    }


    private Long createUser(String name) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        User user = new User();
        user.setName(name);

        tx.begin();
        em.persist(user);
        tx.commit();

        return user.getId();
    }

    private String getName(Long id) {
        EntityManager em = factory.createEntityManager();
        User user = em.find(User.class, id);
        return user.getName();
    }

    @Test
    public void testVersionIncrement() {

        String name = "name";

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        User user = new User();
        user.setName(name);

        tx.begin();
        em.persist(user);
        tx.commit();
        int x = user.getVersion();

        tx.begin();
        user.setName("foo");
        em.merge(user);
        tx.commit();
        int y = user.getVersion();

        assertEquals(x + 1, y);


        tx.begin();
        //no modification of user here
        em.merge(user);
        tx.commit();
        int z = user.getVersion();

        assertEquals(y, z); // version has not been increased
    }


    @Test
    public void testOptimistic() {

        String name = "optimistic";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id,
                LockModeType.OPTIMISTIC); //if @Version is present, OPTIMISTIC is the default anyway
        assertNotNull(u1);

        //check that the user with the given name is in the database
        TypedQuery<Long> query = em.createQuery("select count(u) from User u where u.name = ?1", Long.class);
        query.setParameter(1, name);
        long res = query.getSingleResult();
        assertEquals(1, res);

        //do a sync update on a new thread
        TransactionExecutor executor = new TransactionExecutor(factory);
        executor.syncExe(s -> {
            User user = s.find(User.class, id);
            user.setName("foo");
        });

        //name has been changed now
        res = query.getSingleResult();
        assertEquals(0, res);

        /*
                This is expected: u1 is in a stale state inside the cache e1.
                When we try to commit, JPA detects this fact due to the optimistic lock,
                and so throws an exception
        */
        assertThrows(RollbackException.class, () -> {
            tx.commit();
        });

        em.close();
    }


    @Test
    public void testReadWhilePessimisticLock() {

        String name = "read-pessimistic";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        /*
            WARNING: there are different kinds of PESSIMISTIC locks... but how they are
            handled (if handled at all) does depend on the actual database (eg, H2 vs Postgres).
            Even worse, if some modes are not supported, Hibernate will just silently ignore it...
         */
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_WRITE);
        assertNotNull(u1);

        TransactionExecutor executor = new TransactionExecutor(factory);
        executor.syncExe(s -> {
            User user = s.find(User.class, id);
            String readName = user.getName();
        });

        //as above just read, it is completed by now. recall syncExe is blocking
        tx.commit();
        em.close();
    }

    @Test
    public void testWriteWhilePessimisticLock() throws Exception {

        String name = "write-pessimistic";
        String other = "foo";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_WRITE);
        assertNotNull(u1);

        TransactionExecutor executor = new TransactionExecutor(factory);
        Thread t = executor.asyncExe(s -> {
            User user = s.find(User.class, id);
            user.setName(other); //this will block, as it is a WRITE operation
        });
        //note: if rather used executor.syncExe, this could never be reached, ie a so called Deadlock

        //Note: look at persistence.xml, as lock timeout had to be increased to 10s to make this work
        Thread.sleep(5_000); // simulate a long processing here

        assertTrue(t.isAlive()); //the external transaction is still waiting for the lock

        assertEquals(name, getName(id));

        tx.commit();

        //now that the lock is released, we can wait for the external transaction to finish

        t.join();
        assertFalse(t.isAlive());

        assertEquals(other, getName(id));

        em.close();
    }

}
