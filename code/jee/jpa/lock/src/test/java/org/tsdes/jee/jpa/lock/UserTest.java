package org.tsdes.jee.jpa.lock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.*;

public class UserTest {

    private EntityManagerFactory factory;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @After
    public void tearDown() {
        factory.close();
    }


    private Long createUser(String name){
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        User user = new User();
        user.setName(name);

        tx.begin();
        em.persist(user);
        tx.commit();

        return user.getId();
    }

    private String getName(Long id){
        EntityManager em = factory.createEntityManager();
        User user = em.find(User.class, id);
        return user.getName();
    }

    @Test
    public void testVersionIncrement(){

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
    public void testOptimistic(){

        String name = "optimistic";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id,
                LockModeType.OPTIMISTIC); //if @Version is present, OPTIMISTIC is the default anyway
        assertNotNull(u1);

        //check that the user with the given name is in the database
        Query query = em.createQuery("select count(u) from User u where u.name = '"+name+"'");
        long res = (Long)query.getSingleResult();
        assertEquals(1 , res);

        //do a sync update on a new thread
        TransactionExecutor executor = new TransactionExecutor(factory);
        executor.syncExe(s -> {
            User user = s.find(User.class, id);
            user.setName("foo");
        });

        //name has been changed now
        res = (Long)query.getSingleResult();
        assertEquals(0 , res);

        try {
            tx.commit();
            fail();
        } catch (Exception e){
            /*
                This is expected: u1 is in a stale state inside the cache e1.
                When we try to commit, JPA detects this fact due to the optimistic lock,
                and so throws an exception
             */
        }
        em.close();
    }


    @Test
    public void testPessimisticRead(){

        String name = "pessimisticRead";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_READ);
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
    public void testPessimisticReadWithWrite() throws Exception{

        String name = "pessimisticRead";
        String other = "foo";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_READ);
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


    @Test
    public void testPessimisticWriteWithPessimisticRead() throws Exception{

        String name = "pessimisticWrite";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_WRITE);
        assertNotNull(u1);

        TransactionExecutor executor = new TransactionExecutor(factory);
        Thread t = executor.asyncExe(s -> {
            User user = s.find(User.class, id, LockModeType.PESSIMISTIC_READ);//PESSIMISTIC_WRITE would do as well
            String readName = user.getName(); //just a read operation
        });
        //even if read operation, because lock is PESSIMISTIC_WRITE, still above is blocked

        Thread.sleep(5_000); // simulate a long processing here

        assertTrue(t.isAlive()); //the external transaction is still waiting for the lock

        tx.commit();

        //now that the lock is released, we can wait for the external transaction to finish

        t.join();

        assertFalse(t.isAlive());
        em.close();
    }

    @Test
    public void testPessimisticWriteWithRead() throws Exception{

        String name = "pessimisticWrite";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_WRITE);
        assertNotNull(u1);


        TransactionExecutor executor = new TransactionExecutor(factory);
        Thread t = executor.asyncExe(s -> {
            User user = s.find(User.class, id); //no lock here
            String readName = user.getName(); //just a read operation
        });

        /*
            This can be confusing. if you google for LockModeType, you might read online
            PESSIMISTIC_WRITE does prevent read operations in other transactions.
            this does not really seem true, unless the other transactions do explicitly
            try to use a lock.
            Further note: pessimistic locking is relying on the locking mechanism
            of the actual database, and that might lead to differences among different
            databases
         */

        t.join(2_000);
        assertFalse(t.isAlive());

        tx.commit();
        em.close();
    }


    @Test
    public void testPessimisticReadWithUpdate() throws Exception{

        String name = "pessimisticRead";
        String other = "foo";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        assertEquals(name, getName(id));

        tx.begin();
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_READ);
        assertNotNull(u1);
        //read lock does not prevent us from updating the entity
        u1.setName("foo");
        tx.commit();

        assertEquals(other, getName(id));

        em.close();
    }


    @Test
    public void testPessimisticReadWithPessimisticRead() throws Exception{

        String name = "pessimisticRead";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_READ);
        assertNotNull(u1);

        TransactionExecutor executor = new TransactionExecutor(factory);
        Thread t = executor.asyncExe(s -> {
            User user = s.find(User.class, id, LockModeType.PESSIMISTIC_READ);
            String readName = user.getName(); //just a read operation
        });
        /*
            even if it just a read operation, because it tries to put a lock
            on an already locked entity, it will block.

            Again: if you read JEE documentation, this should not really happen,
            as you should be able to get a read lock if no one else is having
            a write one
         */

        Thread.sleep(5_000); // simulate a long processing here

        assertTrue(t.isAlive()); //the external transaction is still waiting for the lock

        tx.commit();

        //now that the lock is released, we can wait for the external transaction to finish

        t.join();

        assertFalse(t.isAlive());
        em.close();
    }

    /*
        In conclusion:

        pessimistic locks are handled by the database, and not by the JEE container (eg JPA).
        Documentation about them is confusing.
        To be on safe side, just use PESSIMISTIC_WRITE when you need to enforce locking and
        conflicts are expected to be high.
        Do write test cases to check if your understanding of locks is indeed correct.

        By all means, if you find issues in the above tests, contact me ;)
     */
}
