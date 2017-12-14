package org.tsdes.jee.exercises.quizgame.backend.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.tsdes.jee.exercises.quizgame.backend.entity.Category;
import org.tsdes.jee.exercises.quizgame.backend.entity.Quiz;
import org.tsdes.jee.exercises.quizgame.backend.entity.SubCategory;

import javax.ejb.EJB;


public abstract class EjbTestBase {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes.jee.exercises.quizgame")
                .addAsResource("META-INF/persistence.xml");
    }


    @EJB
    private DeleterEJB deleterEJB;

    @EJB
    protected CategoryEjb categoryEjb;

    @EJB
    protected QuizEjb quizEjb;

    @Before
    @After
    public void emptyDatabase() {
        deleterEJB.deleteEntities(Quiz.class);
        deleterEJB.deleteEntities(SubCategory.class);
        deleterEJB.deleteEntities(Category.class);
    }


}
