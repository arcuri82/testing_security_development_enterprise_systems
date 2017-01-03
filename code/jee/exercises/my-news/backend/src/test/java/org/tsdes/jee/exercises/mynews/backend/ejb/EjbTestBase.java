package org.tsdes.jee.exercises.mynews.backend.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.tsdes.jee.exercises.mynews.backend.entity.Post;
import org.tsdes.jee.exercises.mynews.backend.entity.User;
import org.tsdes.jee.exercises.mynews.backend.util.DeleterEJB;

import javax.ejb.EJB;


public abstract class EjbTestBase {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes.jee.exercises.mynews.backend")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    protected UserEJB userEJB;

    @EJB
    protected PostEJB postEJB;


    @EJB
    private DeleterEJB deleterEJB;


    @Before
    @After
    public void emptyDatabase(){
        //deleterEJB.deleteEntities(Post.class);// doesn't work due to @ElementCollection
        postEJB.getAllPostsByTime().stream().forEach(p ->
                deleterEJB.deleteEntityById(Post.class, p.getId()));

        deleterEJB.deleteEntities(User.class);
    }

    protected boolean createUser(String user){
        return userEJB.createUser(user,"foo","a","b","c");
    }

    protected boolean createUser(String user, String password){
        return userEJB.createUser(user,password,"a","b","c");
    }
}
