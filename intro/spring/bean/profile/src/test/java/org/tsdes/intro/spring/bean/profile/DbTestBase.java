package org.tsdes.intro.spring.bean.profile;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.tsdes.intro.spring.bean.jpa.CounterService;

import static org.junit.Assert.assertEquals;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
public abstract class DbTestBase {

    @Autowired
    private CounterService service;

    @Value("${spring.jpa.database}")
    private String databaseName;


    protected abstract String getExpectedDatabaseName();

    @Test
    public void testConfiguration(){
        assertEquals(getExpectedDatabaseName(), databaseName);
    }

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
}
