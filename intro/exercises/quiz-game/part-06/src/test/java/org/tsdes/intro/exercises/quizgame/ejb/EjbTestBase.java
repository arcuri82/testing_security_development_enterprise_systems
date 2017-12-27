package org.tsdes.intro.exercises.quizgame.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

@RunWith(Arquillian.class)
public abstract class EjbTestBase {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes.intro.exercises.quizgame")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private ResetEjb resetEjb;

    @Before
    public void resetDatabase(){
        resetEjb.resetDatabase();
    }
}
