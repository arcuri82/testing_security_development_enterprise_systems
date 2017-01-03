package org.tsdes.jee.jpa.lock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookTest {

    private EntityManagerFactory factory;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @After
    public void tearDown() {
        factory.close();
    }

    private boolean changeTitle(EntityManager em, Long shelf, Long book, LockModeType type){

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Book x = em.find(Book.class, book, type);
        x.setTitle("A new title");

        //before this transaction is committed, add book to the shelf
        TransactionExecutor executor = new TransactionExecutor(factory);
        executor.syncExe(t -> {
            //note, here the book does not get modified
            Book y = t.find(Book.class, book, type);
            Shelf b = t.find(Shelf.class, shelf);
            b.getBooks().add(y);
        });

        try {
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            System.out.println("\n\nFailed transaction: "+e.getCause().toString()+"\n\n");
            return false;
        }
        return true;
    }

    private boolean isAdded(LockModeType type) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        Shelf shelf = new Shelf();
        Book book = new Book();

        tx.begin();
        em.persist(shelf);
        em.persist(book);
        tx.commit();

        return changeTitle(em, shelf.getId(), book.getId(), type);
    }


    @Test
    public void testBook(){

        boolean added = isAdded(LockModeType.OPTIMISTIC);
        assertTrue(added);
    }


    @Test
    public void testBookForceIncrement(){

        /*
            OPTIMISTIC_FORCE_INCREMENT is quite rare.
            It is needed when in a transaction an entity is not modified, but still
            the fact that it has participated in the transaction should rollback
            all the other transactions currently using such entity.
            For example, adding X to a list in Y (eg unidirectional OneToMany relation)
            does not change X.

            Note: in this example, you might think a business case would be to prevent
            two transactions each one adding the same book to a different shelf.
            Yes, that would do, but unnecessary, as the second transaction to complete
            would fail anyway due to constraint violation of OneToMany (instead it would
            had work for ManyToMany)
         */
        boolean added = isAdded(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        assertFalse(added);
    }

}
