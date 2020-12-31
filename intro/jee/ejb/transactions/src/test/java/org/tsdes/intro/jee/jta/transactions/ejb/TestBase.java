package org.tsdes.intro.jee.jta.transactions.ejb;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.ejb.EJB;


@RunWith(Arquillian.class)
public abstract class TestBase {

    @EJB
    protected QueriesEJB queriesEJB;

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(
                        TestBase.class,
                        EJB_01_REQUIRED.class,
                        EJB_02_abort.class,
                        EJB_03_rollback.class,
                        EJB_04_SUPPORTS.class,
                        EJB_05_REQUIRES_NEW.class,
                        EJB_06_proxy.class,
                        EJB_07_MANDATORY.class,
                        EJB_08_NOT_SUPPORTED.class,
                        EJB_09_NEVER.class,
                        EJB_10_multi_base.class,
                        EJB_10_multi_caller.class,
                        EJB_11_exceptions.class,
                        EJB_12_status.class,
                        QueriesEJB.class,
                        Foo.class
                )
                //besides the classes, also need to add resources
                .addAsResource("META-INF/persistence.xml");
    }


    @Before
    @After
    public void emptyDatabase(){
        //this is quicker than re-initialize the whole DB / EJB container
        queriesEJB.deleteAll();
    }

}
