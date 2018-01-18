package org.tsdes.intro.jee.ejb.arquillian;

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
                //besides the classes, also need to add resources
                .addAsResource("META-INF/persistence.xml");
    }


    @EJB
    private A singleton;

    /*
        By default, Arquillian does one single deployment per test
        class, which means that the tests share the same state.
        Need to be careful of side effects between tests (eg on the DB and EJBs),
        as each test should work when run in isolation and when run with others
        in ANY order.

        In this particular case, 2 tests will be marked as "ignore",
        as the "assume" check fails because the state has been modified
        by a previous test.
        But which tests will be ignored depend on the test order.

        A test can either "pass", "fail" or be "ignored"
     */


    @Test
    public void testOnceSingleton(){

        assumeTrue(singleton.getInternalX() == 0);

        checkAndModifyInternalState();
    }

    @Test
    public void testASecondTimeSingleton(){

        assumeTrue(singleton.getInternalX() == 0);

        checkAndModifyInternalState();
    }

    @Test
    public void testOnceDB(){

        assumeTrue(singleton.readDB() == 0);

        checkAndModifyDB();
    }

    @Test
    public void testASecondTimeDB(){

        assumeTrue(singleton.readDB() == 0);

        checkAndModifyDB();
    }


    private void checkAndModifyInternalState(){
        assertEquals(0, singleton.getInternalX());
        singleton.incrementInternalX();
        assertEquals(1, singleton.getInternalX());
    }

    private void checkAndModifyDB(){
        assertEquals(0, singleton.readDB());
        singleton.updateDB(1);
        assertEquals(1, singleton.readDB());
    }
}