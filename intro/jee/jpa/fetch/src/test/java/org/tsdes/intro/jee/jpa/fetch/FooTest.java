package org.tsdes.intro.jee.jpa.fetch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by arcuri82 on 10-Jan-17.
 */
public class FooTest {

    private EntityManagerFactory factory;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @After
    public void tearDown() {
        factory.close();
    }

    private boolean executeInTransaction(Consumer<EntityManager> lambda) {

        EntityManager manager = factory.createEntityManager();
        EntityTransaction tx = manager.getTransaction();
        tx.begin();

        try {
            lambda.accept(manager);
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        } finally {
            manager.close();
        }

        return true;
    }

    private Foo getById(long id) {

        EntityManager em = factory.createEntityManager();

        try {
            Query query = em.createQuery("select f from Foo f where f.id=?1");
            query.setParameter(1, id);

            List list = query.getResultList();
            if (list.isEmpty()) {
                return null;
            } else {
                return (Foo) list.get(0);
            }
        } finally {
            em.close();
        }
    }

    private Foo getByIdForcingLoading(long id) {

        EntityManager em = factory.createEntityManager();

        try {
            //note: using TypedQuery just to show you how to avoid casting
            TypedQuery<Foo> query = em.createQuery(
                    //here we force the loading of f.lazyBi
                    "select f from Foo f left join fetch f.lazyBi where f.id=?1", Foo.class);
            query.setParameter(1, id);

            List<Foo> list = query.getResultList();
            if (list.isEmpty()) {
                return null;
            } else {
                return list.get(0); // no (Foo cast here)
            }

        } finally {
            em.close();
        }
    }

    @Test
    public void testFetchEager() {

        long id = 1;

        Foo foo = new Foo();
        foo.setId(id);
        Bar bar = new Bar();
        foo.getEagerBi().add(bar);
        bar.setParent(foo);

        boolean executed = executeInTransaction(em -> {
            em.persist(foo);
            em.persist(bar);
        });

        assertTrue(executed);

        /*
            here we are outside of a transaction, but still reading data
            should be fine, as such data has already been loaded and stored
            in the foo entity
         */

        Foo readBack = getById(id);

        assertEquals(1, readBack.getEagerBi().size());
    }

    @Test
    public void testFetchLazy() {

        long id = 1;

        Foo foo = new Foo();
        foo.setId(id);
        Bar bar = new Bar();
        foo.getLazyBi().add(bar);
        bar.setParent(foo);

        boolean executed = executeInTransaction(em -> {
            em.persist(foo);
            em.persist(bar);
        });

        assertTrue(executed);

        /*
            Why the following fails?
            because the list is lazy, to load it we need to access it
            from an active session (eg inside of a transaction).

            Note: we will see more about this type of issues when
            working with EJB
         */

        Foo readBack = getById(id);

        try {
            readBack.getLazyBi().size();
            fail(); //recall, this throw Error and not Exception
        } catch (Exception e) {
            //expected
        }

        //following should be fine
        readBack = getByIdForcingLoading(id);

        assertEquals(1, readBack.getLazyBi().size());
    }


    @Test
    public void testDeleteBidirectional() {

        long id = 1;

        Foo foo = new Foo();
        foo.setId(id);
        Bar bar = new Bar();
        foo.getLazyBi().add(bar);
        bar.setParent(foo);

        boolean executed = executeInTransaction(em -> {
            em.persist(foo);
            em.persist(bar);
        });

        assertTrue(executed);

        assertNotNull(getById(id));

        boolean deleted = executeInTransaction(em -> {
            Foo x = em.find(Foo.class, id);
            em.remove(x);
        });

        assertTrue(deleted);
        assertNull(getById(id));
    }
}