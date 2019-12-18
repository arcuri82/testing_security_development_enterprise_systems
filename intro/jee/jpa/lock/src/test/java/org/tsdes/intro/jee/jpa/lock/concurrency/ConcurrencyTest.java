package org.tsdes.intro.jee.jpa.lock.concurrency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ConcurrencyTest {

    private EntityManagerFactory factory;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @AfterEach
    public void tearDown() {
        factory.close();
    }


    private Long createNewCounter(Class<? extends Counter> klass) throws Exception {

        Counter counter = klass.getDeclaredConstructor().newInstance();

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {
            em.persist(counter);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }

        em.close();

        return counter.getId();
    }

    private boolean incrementCounter(Counter counter, EntityManager em, LockModeType lockModeType){

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            //first read what is the current state in database
            em.refresh(counter, lockModeType);

            counter.increment();

            em.merge(counter); //merge back into database

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            return false;
        }

        return true;
    }


    private int runInParallel(Long id,
                              LockModeType lockModeType,
                              boolean retryIfFails,
                              BiFunction<EntityManager, Long, Counter> provider) {

        final int nThreads = 4;
        final int loops = 10_000;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {

            Thread t = new Thread() {
                @Override
                public void run() {

                    EntityManager em = factory.createEntityManager();
                    Counter counter = provider.apply(em, id);

                    int executions = 0;

                    while(executions < loops){
                        if(! em.contains(counter)){
                            //in some cases, a failed increment can detach the entity
                            counter = provider.apply(em, id);
                        }

                        boolean success = incrementCounter(counter, em, lockModeType);
                        if(success || !retryIfFails){
                            executions++;
                        }
                    }

                    em.close();
                }
            };
            t.start();
            threads.add(t);
        }

        //Could have used just a loop
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });

        EntityManager em = factory.createEntityManager();
        Counter counter = provider.apply(em, id);
        em.close();

        int result = counter.getCounter();
        int expected = nThreads * loops;
        int diff = expected - result ;

        return diff;
    }

    private void printMissingIterations(int delta){
        System.out.println("\nMISSING ITERATIONS: " + delta +"\n\n");
    }

    //-----------------------------------------------------------

    @Test
    public void testNoLockControl() throws Exception{

        Class<BaseCounter> klass = BaseCounter.class;

        Long id = createNewCounter(klass);

        // this should not have effect, as transactions do not fail, they just write rubbish
        boolean retryIfFails = true;

        int delta = runInParallel(id, LockModeType.NONE, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        assertTrue(delta > 0);//no way that at least one transaction did not fail
    }

    /*
            Optimistic or pessimistic lock?

            Optimistic: assumption that conflicts are rare. Throws exception if conflict is detected,
            and then leave the user to handle it (eg retry). This is not cheap, but, if it happens rarely,
            not a big deal

            Pessimistic: safer/easier, but database locking is expensive. So should use it only when there
            is high chance of conflicts
     */



    @Test
    public void testPessimisticWriteLock() throws Exception{

        Class<BaseCounter> klass = BaseCounter.class;
        Long id = createNewCounter(klass);

        //no need to retry, as no transaction should fail.
        //Note: a pessimistic lock does throw an exception if waiting too long (ie a timeout, default 1 second)
        boolean retryIfFails = false;

        int delta = runInParallel(id, LockModeType.PESSIMISTIC_WRITE, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        assertEquals(0, delta);
    }

    @Test
    public void testOptimisticButNoRetry() throws Exception{

        Class<CounterWithVersion> klass = CounterWithVersion.class;
        Long id = createNewCounter(klass);

        boolean retryIfFails = false;

        int delta = runInParallel(id, LockModeType.OPTIMISTIC, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        assertTrue(delta > 0); //some transactions would fail due to detected inconsistencies
    }

    @Test
    public void testOptimisticButNoVersion() throws Exception{

        Class<BaseCounter> klass = BaseCounter.class;
        Long id = createNewCounter(klass);

        boolean retryIfFails = true;

        int delta = runInParallel(id, LockModeType.OPTIMISTIC, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        //still fails, even if repeat failed transaction, because no failed transaction in the first place
        assertTrue(delta > 0);
    }

    @Test
    public void testOptimisticWithRetry() throws Exception{

        Class<CounterWithVersion> klass = CounterWithVersion.class;
        Long id = createNewCounter(klass);

        boolean retryIfFails = true;

        int delta = runInParallel(id, LockModeType.OPTIMISTIC, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        assertEquals(0, delta); //failed transactions are repeated
    }
}
