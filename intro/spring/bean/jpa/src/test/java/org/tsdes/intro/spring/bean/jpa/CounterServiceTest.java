package org.tsdes.intro.spring.bean.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Note: Spring provides some utilities that are specialized for testing JPA
 * code, like making the tests transactional and rollback at method end instead
 * of committing the transaction (with the goal of now altering the DB).
 * But I am not a big fun of them... I much prefer have proper integration tests
 * with actual committed transactions, and handle cleaning of side-effects
 * manually (if necessary).
 *
 * Created by arcuri82 on 26-Jan-18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CounterServiceTest {

    @Autowired
    private CounterService service;


    @Test
    public void testCreateAndIncrement(){

        long id = service.createNewCounter();
        long x = service.getValueForCounter(id);

        service.increment(id);

        long y = service.getValueForCounter(id);

        assertEquals(x + 1, y);
    }

    @Test
    public void testIncrementNotInATransaction(){

        long id = service.createNewCounter();
        long x = service.getValueForCounter(id);

        /*
           WARNING: this is a write operation not-done
           in a transaction... but it will NOT throw
           an exception, simply the operation will be on
           the em cache
         */
        service.incrementNotInTransaction(id);

        long y = service.getValueForCounter(id);

        //value in DB has not been modified
        assertEquals(x, y);
    }
}
