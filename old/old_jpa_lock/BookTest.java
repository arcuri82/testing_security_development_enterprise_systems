package org.tsdes.intro.jee.jpa.lock.forceincrement;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tsdes.intro.jee.jpa.lock.TransactionExecutor;

import javax.persistence.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookTest {

    private EntityManagerFactory factory;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @AfterEach
    public void tearDown() {
        factory.close();
    }

    private boolean changeTitle(EntityManager em, Long shelf, Long bookId, LockModeType type){

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Book x = em.find(Book.class, bookId, type);
        x.setTitle("A new title");

        //before this transaction is committed, add book to the shelf
        TransactionExecutor executor = new TransactionExecutor(factory);
        executor.syncExe(t -> {
            /*
                note, here the book @Entity does not get modified, although
                the table is (recall how 1-to-Many relationships are represented
                with FKs)
             */
            Book y = t.find(Book.class, bookId, type);
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
            OPTIMISTIC_FORCE_INCREMENT is less common.
            It is needed when in a transaction an entity is not modified, but still
            the fact that it has participated in the transaction should rollback
            all the other transactions currently using such entity.
            For example, adding X to a list in Y (eg unidirectional OneToMany relation)
            does not change the entity X, although it does change the table mapped by
            X (ie the FK pointing to Y).

            A business case would be to prevent a transaction adding a book to a shelf while
            another concurrent transaction added the same book to a different shelf.
         */
        boolean added = isAdded(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        assertFalse(added);
    }

}
