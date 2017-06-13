package org.tsdes.jee.ejb.arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

@RunWith(Arquillian.class)
public class SingletonTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(A.class, Data.class)
                .addAsResource("META-INF/persistence.xml");
    }


    @EJB
    private A singleton;

    /*
        Careful: it looks like Arquillian does one single deployment per test
        class, which means that the tests share the same state.
        Need to be careful of side effects between tests (eg on the DB and EJBs),
        as each test should work when run in isolation and when run with others
        in ANY order.
     */


    @Test
    public void testOnceSingleton(){

        assumeTrue(singleton.getInternalX() == 0);

        checkInternalState();
    }

    @Test
    public void testASecondTimeSingleton(){

        assumeTrue(singleton.getInternalX() == 0);

        checkInternalState();
    }

    @Test
    public void testOnceDB(){

        assumeTrue(singleton.readDB() == 0);

        checkDB();
    }

    @Test
    public void testASecondTimeDB(){

        assumeTrue(singleton.readDB() == 0);

        checkDB();
    }


    private void checkInternalState(){
        assertEquals(0, singleton.getInternalX());
        singleton.incrementInternalX();
        assertEquals(1, singleton.getInternalX());
    }

    private void checkDB(){
        assertEquals(0, singleton.readDB());
        singleton.updateDB(1);
        assertEquals(1, singleton.readDB());
    }
}