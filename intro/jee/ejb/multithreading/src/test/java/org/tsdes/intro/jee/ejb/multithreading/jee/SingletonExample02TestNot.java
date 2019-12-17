package org.tsdes.intro.jee.ejb.multithreading.jee;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;
import org.tsdes.intro.jee.ejb.multithreading.Counter;
import org.tsdes.intro.jee.ejb.multithreading.CounterTestBase;


import javax.ejb.EJB;


@RunWith(Arquillian.class)
public class SingletonExample02TestNot extends CounterTestBase {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Counter.class, SingletonExample02.class, CounterTestBase.class);
    }

    @EJB
    private Counter counter;

    @Override
    protected Counter getCounter() {
        return counter;
    }
}