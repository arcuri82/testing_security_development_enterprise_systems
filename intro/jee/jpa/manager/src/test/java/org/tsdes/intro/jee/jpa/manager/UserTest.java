package org.tsdes.intro.jee.jpa.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

public class UserTest {

    private EntityManagerFactory factory;
    private EntityManager em;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    private boolean persistInATransaction(Object... obj) {
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

    @Test
    public void testPersistAndFind(){

        String name = "foo";

        User user = new User();
        user.setName(name);

        persistInATransaction(user);

        //make sure we clear the cache, making all entity objects detached
        em.clear();

        //read operations like this do not need an explicit transaction
        User found = em.find(User.class, user.getId());

        assertEquals(name, found.getName());
    }

    @Test
    public void testPersistAndModify() {

        String before = "before";
        String during = "during";
        String after = "after";

        User user = new User();
        user.setName(before);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(user);
            /*
                the transaction is not finished, so, any modification to the attached
                object will be committed to the DB
             */
            user.setName(during);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail();
        }

        //this is done after the transaction, so no effect on DB
        user.setName(after);

        //make sure we clear the cache, making all entity objects detached
        em.clear();

        //read operations like this do not need an explicit transaction
        User found = em.find(User.class, user.getId());

        assertEquals(during, found.getName());
        assertEquals(after, user.getName());
    }



    @Test
    public void testDoublePersist(){

        String first = "first";
        String second = "second";

        User user = new User();
        user.setName(first);

        assertTrue(persistInATransaction(user));

        Long id = user.getId();
        assertNotNull(id);

        assertTrue(em.contains(user));

        user.setName(second);
        assertTrue(persistInATransaction(user));

        em.clear();

         /*
            The JavaDoc of em.persist() states:
            Throws:
            EntityExistsException - if the entity already exists.
            (If the entity already exists, the EntityExistsException may be thrown when the persist operation
            is invoked, or the EntityExistsException or another PersistenceException may be thrown at flush
            or commit time.)

            However, here it looks more like it is just doing an update of the entity
         */

        User found = em.find(User.class, user.getId());
        assertEquals(second, found.getName());
        assertEquals(id, found.getId());

        /*
            but what if we detach the entity from the EntityManager?
         */

        em.clear();
        assertFalse(em.contains(user));

        /*
            This fails because:
            1) there exists already a User row in the database with same @Id
            2) the entity is not already in the EntityManager, ie it is detached
         */

        assertFalse(persistInATransaction(user));
    }



    @Test
    public void testMerge(){

        String before = "before";
        String after = "after";

        User user = new User();
        user.setName(before);

        assertTrue(persistInATransaction(user));

        //this is done after the transaction, so no effect on DB
        user.setName(after);

        //make sure we clear the cache, making all entity objects detached
        em.clear();

        //read operations like this do not need an explicit transaction
        User found = em.find(User.class, user.getId());

        assertEquals(before, found.getName());
        assertEquals(after, user.getName());

        //now we "merge" the changes to the detached User instance into the DB
        EntityTransaction tx = em.getTransaction();
        em.contains(user);
        tx.begin();
        em.merge(user);
        tx.commit();

        em.clear();

        //after the merge, the "name" field should had been updated
        found = em.find(User.class, user.getId());
        assertEquals(after, found.getName());
    }


    @Test
    public void testRefresh(){

        String before = "before";
        String after = "after";

        User user = new User();
        user.setName(before);

        assertTrue(persistInATransaction(user));

        user.setName(after);

        assertEquals(after, user.getName());

        //we can only do a "refresh" on a managed entity, not detached ones
        //em.clear();

        //now we "refresh" the changes done after the transaction
        EntityTransaction tx = em.getTransaction();
        em.contains(user);
        tx.begin();
        em.refresh(user); //this practically update the entity with values from the DB
        tx.commit();

        em.clear();

        assertEquals(before, user.getName());
    }


    @Test
    public void testDetach(){

        String name = "foo";

        User user = new User();
        user.setName(name);

        assertTrue(persistInATransaction(user));

        assertTrue(em.contains(user));

        em.detach(user);

        assertFalse(em.contains(user));

        //"refresh" fails, as entity is detached
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.refresh(user);
            tx.commit();
            fail();
        } catch (Exception e){
            tx.rollback();
        }

        //being detached from the EntityManager does not mean being removed from DB
        User found = em.find(User.class, user.getId());
        assertEquals(name, found.getName());
    }


    @Test
    public void testRemove(){
        String name = "foo";

        User user = new User();
        user.setName(name);

        assertTrue(persistInATransaction(user));
        assertTrue(em.contains(user));

        User found = em.find(User.class, user.getId());
        assertEquals(name, found.getName());


        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(user);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            fail();
        }

        assertFalse(em.contains(user));

        //can't be found anymore in the DB
        found = em.find(User.class, user.getId());
        assertNull(found);
    }


    @Test
    public void testOrphanRemovalOwner(){

        Address address = new Address();
        User user = new User();
        user.setAddress(address);

        //User refers to unmanaged Address, and no cascade for persist
        assertFalse(persistInATransaction(user));

        user.setId(null);

        assertTrue(persistInATransaction(user,address));

        Address found = em.find(Address.class, address.getId());
        assertNotNull(found);

        //now remove User from DB
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(user);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            fail();
        }

        //User is owner of the OneToOne relation, so, because of "orphanRemoval,
        //address get removed as well
        found = em.find(Address.class, address.getId());
        assertNull(found);
    }


    @Test
    public void testOrphanRemovalFail(){

        Address address = new Address();
        User user = new User();
        user.setAddress(address);

        assertTrue(persistInATransaction(user,address));

        User found = em.find(User.class, user.getId());
        assertNotNull(found.getAddress());

        //now try to remove Address from DB
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(address);
            tx.commit();
            fail();
        } catch (Exception e){
            tx.rollback();
        }

        //the remove should had fail, because User is owner and still in DB

        found = em.find(User.class, user.getId());
        assertNotNull(found.getAddress());
    }
}