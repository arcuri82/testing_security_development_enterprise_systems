package org.tsdes.intro.jee.ejb.query;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tsdes.misc.testutils.EmbeddedJeeSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TopUsersTest {

    private static EmbeddedJeeSupport container = new EmbeddedJeeSupport();

    @BeforeEach
    public void initContainer()  {
        container.initContainer();
        initDB();
    }

    @AfterEach
    public void closeContainer() throws Exception {
        container.closeContainer();
    }


    private void initDB(){

        createUser("a", 0, 0);
        createUser("b", 3, 2);
        createUser("c", 2, 4);
        createUser("d", 0, 1);
        createUser("e", 1, 3);

        //sorted: c,b,e,d,a
    }

    private void createUser(String name, int nComments, int nPosts){

        UserEJB ejb = container.getEJB(UserEJB.class);
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

        UserEJB ejb = container.getEJB(UserEJB.class);
        List<User> list = ejb.getTopUsersUsingCounter(3);
        test(list);
    }

    @Test
    public void testWithoutCounter(){

        UserEJB ejb = container.getEJB(UserEJB.class);
        List<User> list = ejb.getTopUsersWithoutCounter(3);
        test(list);
    }

    private void test(List<User> list){

        assertEquals(3, list.size());

        assertEquals("c", list.get(0).getName());
        assertEquals("b", list.get(1).getName());
        assertEquals("e", list.get(2).getName());
    }



}