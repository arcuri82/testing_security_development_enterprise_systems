package org.tsdes.jee.ejb.query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TopUsersTest {

    protected static EJBContainer ec;
    protected static Context ctx;

    @Before
    public void initContainer() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put(EJBContainer.MODULES, new File("target/classes"));
        ec = EJBContainer.createEJBContainer(properties);
        ctx = ec.getContext();

        initDB();
    }

    protected <T> T getEJB(Class<T> klass){
        try {
            return (T) ctx.lookup("java:global/classes/"+klass.getSimpleName()+"!"+klass.getName());
        } catch (NamingException e) {
            return null;
        }
    }

    @After
    public void closeContainer() throws Exception {
        if (ctx != null)
            ctx.close();
        if (ec != null)
            ec.close();
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

        UserEJB ejb = getEJB(UserEJB.class);
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

        UserEJB ejb = getEJB(UserEJB.class);
        List<User> list = ejb.getTopUsersUsingCounter(3);
        test(list);
    }

    @Test
    public void testWithoutCounter(){

        UserEJB ejb = getEJB(UserEJB.class);
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