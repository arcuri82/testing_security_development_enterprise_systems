package org.tsdes.jee.exercises.mycantina.backend.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.tsdes.jee.exercises.mycantina.backend.entity.Dish;
import org.tsdes.jee.exercises.mycantina.backend.entity.Menu;

import javax.ejb.EJB;


public abstract class EjbTestBase {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes.jee.exercises.mycantina.backend")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    protected DishEJB dishEJB;

    @EJB
    protected MenuEJB menuEJB;

    @EJB
    private DeleterEJB deleterEJB;


    @Before
    @After
    public void emptyDatabase() {
        deleterEJB.deleteEntities(Menu.class);
        deleterEJB.deleteEntities(Dish.class);
    }


}
