package org.tsdes.jee.ejb.singleton;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.ejb.EJB;

import static org.junit.Assert.*;

/*
    Before running this test from IntelliJ, you need to execute:

    "mvn test"

    this will compile the project, and execute the Maven plugin that download
    WildFly under the "target/" folder. You need it just once, until you do a
    "mvn clean" (which deletes what is inside "target/")

    When running from IntelliJ with "Run ...", need to specify:
    - "Manual container configuration" under "Arquillian Containers"
    - Under "Configuration", the "Working directory" should be "$MODULE_DIR$"

    To avoid doing it for all tests, you can set them once and for all under
    "Defaults -> Arquillian JUnit"

 */

/*
    The RunWith is to have a special, customized way to run tests.
    In the particular case of Arquillian, it handles the automated deployment
    to the JEE container and dependency injection directly in the test
 */
@RunWith(Arquillian.class)
public class CounterTest {

    @Deployment
    public static JavaArchive createDeployment() {

        /*
            This creates a Jar file containing the given classes, and
            then deploy it to WildFly
         */

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Counter.class, A.class, B.class);
    }


    /*
        The same EJB in the JEE container can be injected here in the test,
        so we can call them directly
     */
    @EJB
    private A a;
    @EJB
    private B b;
    @EJB
    private Counter counter;


    @Test
    public void testSingleton(){

        /*
            Because Counter is a singleton, we are sure that the references injected
            into A and B is the same instance
         */

        a.incrementCounter();
        b.incrementCounter();
        counter.increment();

        assertEquals(3, counter.get());
    }

}