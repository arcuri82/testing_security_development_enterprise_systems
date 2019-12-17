package org.tsdes.intro.jee.ejb.query;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

@RunWith(Arquillian.class)
public class TopUsersTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Comment.class, Post.class, User.class, UserEJB.class)
                //besides the classes, also need to add resources
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB ejb;

    private void createUser(String name, int nComments, int nPosts){

        long id = ejb.createUser(name);

        for(int i=0; i<nComments; i++){
            ejb.createComment(id,"comment: "+i);
        }
        for(int i=0; i<nPosts; i++){
            ejb.createPost(id, "post: "+i);
        }
    }


    @Test
    public void testUsingCounter(){

        createUser("a", 0, 0);
        createUser("b", 3, 2);
        createUser("c", 2, 4);
        createUser("d", 0, 1);
        createUser("e", 1, 3);
        //sorted: c,b,e,d,a

        List<User> with = ejb.getTopUsersUsingCounter(3);
        test(with);

        List<User> without = ejb.getTopUsersWithoutCounter(3);
        test(without);
    }

    private void test(List<User> list){

        assertEquals(3, list.size());

        assertEquals("c", list.get(0).getName());
        assertEquals("b", list.get(1).getName());
        assertEquals("e", list.get(2).getName());
    }



}