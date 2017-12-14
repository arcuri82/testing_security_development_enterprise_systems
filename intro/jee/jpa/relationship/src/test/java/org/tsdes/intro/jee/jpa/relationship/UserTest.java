package org.tsdes.intro.jee.jpa.relationship;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void testEmptyUser(){

        User user = new User();
        assertTrue(persistInATransaction(user));
    }


    @Test
    public void testUserWithAddress(){

        Address address = new Address();
        AddressWithUserLink addressWithUserLink = new AddressWithUserLink();

        //being an entity, and not an embedded element, can directly save to database
        assertTrue(persistInATransaction(address));
        assertTrue(persistInATransaction(addressWithUserLink));


        assertNull(addressWithUserLink.getUser());

        User user = new User();
        user.setAddress(address);
        user.setAddressWithUserLink(addressWithUserLink);
        addressWithUserLink.setUser(user);

        assertTrue(persistInATransaction(user));
    }


    @Test
    public void testMessagesFail(){

        User user = new User();

        //the list is not initialized
        assertNull(user.getSentMessages());

        assertTrue(persistInATransaction(user));

        //even after persisting object to DB, the list is not initialized
        assertNull(user.getSentMessages());


        User anotherUser = new User();

        Message msg = new Message();
        anotherUser.setSentMessages(new ArrayList<>());
        anotherUser.getSentMessages().add(msg);

       /*
            This fails because actions on sentMessages are not cascaded to its values, ie
            the msg objects
         */
        assertFalse(persistInATransaction(anotherUser));
    }


    @Test
    public void testMessagesPassByResettingId(){

        User user = new User();

        Message msg = new Message();
        user.setSentMessages(new ArrayList<>());
        user.getSentMessages().add(msg);

        assertNull(user.getId());
        assertFalse(persistInATransaction(user));

        /*
            This is tricky: even if the previous transaction failed and rolledback,
            the state of the Java object "user" in the JVM has been changed (ie
            it got an ID).
         */
        assertNotNull(user.getId());

        //failed, as "user" is considered as a detached entity because @Id != null and
        //there is no table row with that id
        assertFalse(persistInATransaction(msg, user));

        msg.setId(null);
        user.setId(null);


        //order in which they are persisted does not matter, as they are in same transaction
        assertTrue(persistInATransaction(msg, user));
    }


    @Test
    public void testMessagesPass(){

        User user = new User();

        Message msg = new Message();
        user.setSentMessages(new ArrayList<>());
        user.getSentMessages().add(msg);

        assertTrue(persistInATransaction(user, msg));
    }


    @Test
    public void testMessagesWithUserLink(){

        User user = new User();

        MessageWithUserLink msg = new MessageWithUserLink();
        msg.setSender(user);
        user.setSentMessagesWithSenderLink(new ArrayList<>());
        user.getSentMessagesWithSenderLink().add(msg);

        /*
            Note: here do not need to explicitly persist the
            "msg", as sentMessagesWithSenderLink is cascading it on
            its elements
         */
        assertTrue(persistInATransaction(user));
    }


    @Test
    public void testAssignments(){

        User u0 = new User();
        User u1 = new User();

        GroupAssignment g0 = new GroupAssignment();
        g0.setId(0L);
        GroupAssignment g1 = new GroupAssignment();
        g1.setId(1L);

        //link all users to all assignments

        u0.setAssignments(new HashMap<>());
        u1.setAssignments(new HashMap<>());

        u0.getAssignments().put(g0.getId(), g0);
        u0.getAssignments().put(g1.getId(), g1);
        u1.getAssignments().put(g0.getId(), g0);
        u1.getAssignments().put(g1.getId(), g1);

        g0.setAuthors(new ArrayList<>());
        g1.setAuthors(new ArrayList<>());

        g0.getAuthors().add(u0);
        g0.getAuthors().add(u1);
        g1.getAuthors().add(u0);
        g1.getAuthors().add(u1);

        // no cascade, so need to persist them all explicitly
        assertTrue(persistInATransaction(u0,u1,g0,g1));
    }

    @Test
    public void testElementCollection(){

        User u = new User();
        u.setRoles(new ArrayList<>());

        u.getRoles().add("Admin");

        assertTrue(persistInATransaction(u));
    }
}