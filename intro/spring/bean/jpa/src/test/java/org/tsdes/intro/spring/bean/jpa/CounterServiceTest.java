package org.tsdes.intro.spring.bean.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CounterServiceTest {

    @Autowired
    private CounterService service;


    @Test
    public void testCreateAndIncrement(){

        long id = service.createNewCounter();
        long x = service.getValueForCounter(id);

        service.increment(id);

        //just make sure we are going to read from DB and avoid cache
        service.clearCache();

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

        //just make sure we are going to read from DB and avoid cache
        service.clearCache();

        long y = service.getValueForCounter(id);

        //value in DB has not been modified
        assertEquals(x, y);
    }
}
