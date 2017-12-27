package org.tsdes.intro.jee.jsf.examples.ex05;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.*;
import org.junit.runner.RunWith;
import org.tsdes.intro.jee.jsf.examples.test.DeleterEJB;
import org.tsdes.intro.jee.jsf.examples.ex05.ejb.UserEJB;
import org.tsdes.intro.jee.jsf.examples.ex05.entity.UserDetails;
import org.tsdes.intro.jee.jsf.examples.test.DeleterEJB;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, UserEJBTest.class.getPackage().getName())
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;

    @EJB
    private DeleterEJB deleterEJB;


    @Before @After
    public void emptyDatabase(){
       deleterEJB.deleteEntities(UserDetails.class);
    }


    @Test
    public void testCanCreateAUser(){

        String user = "user";
        String password = "password";

        boolean created = userEJB.createUser(user,password);
        assertTrue(created);
    }


    @Test
    public void testNoTwoUsersWithSameId(){

        String user = "user";

        boolean created = userEJB.createUser(user,"a");
        assertTrue(created);

        created = userEJB.createUser(user,"b");
        assertFalse(created);
    }

    @Test
    public void testSamePasswordLeadToDifferentHashAndSalt(){

        String password = "password";
        String first = "first";
        String second = "second";

        userEJB.createUser(first,password);
        userEJB.createUser(second,password); //same password

        UserDetails f = userEJB.getUser(first);
        UserDetails s = userEJB.getUser(second);

        //those are EXTREMELY unlikely to be equal, although not impossible...
        //however, likely more chances to get hit in the head by a meteorite...
        assertNotEquals(f.getHash(), s.getHash());
        assertNotEquals(f.getSalt(), s.getSalt());
    }

    @Test
    public void testVerifyPassword(){

        String user = "user";
        String correct = "correct";
        String wrong = "wrong";

        userEJB.createUser(user, correct);

        boolean  canLogin = userEJB.login(user, correct);
        assertTrue(canLogin);

        canLogin = userEJB.login(user, wrong);
        assertFalse(canLogin);
    }

    @Test
    public void testBeSurePasswordIsNotStoredInPlain(){

        String user = "user";
        String password = "password";

        userEJB.createUser(user, password);

        UserDetails entity = userEJB.getUser(user);
        assertNotEquals(password, entity.getUserId());
        assertNotEquals(password, entity.getHash());
        assertNotEquals(password, entity.getSalt());
    }

}