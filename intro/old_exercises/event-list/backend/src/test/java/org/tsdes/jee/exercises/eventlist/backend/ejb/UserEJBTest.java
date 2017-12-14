package org.tsdes.jee.exercises.eventlist.backend.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.jee.exercises.eventlist.backend.entity.User;
import org.tsdes.jee.exercises.eventlist.backend.util.DeleterEJB;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes.jee.exercises.eventlist.backend")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;
    @EJB
    private EventEjb eventEjb;
    @EJB
    private DeleterEJB deleterEJB;


    @Before
    @After
    public void emptyDatabase(){
        deleterEJB.deleteEntities(User.class);
    }


    private boolean createUser(String user, String password){
        return userEJB.createUser(user,password,"a","b","c","Norway");
    }

    @Test
    public void testCanCreateAUser(){

        String user = "user";
        String password = "password";

        boolean created = createUser(user,password);
        assertTrue(created);
    }


    @Test(expected = EJBException.class)
    public void testCreateAUserWithWrongCountry(){

        String user = "user";
        String password = "password";

        userEJB.createUser(user,password,"a","b","c","FOO");
    }

    @Test(expected = EJBException.class)
    public void testCreateAUserWithWrongId(){

        String user = "user!!!";
        String password = "password";

        createUser(user,password);
    }

    @Test(expected = EJBException.class)
    public void testCreateAUserWithEmpty(){

        String user = "    ";
        String password = "password";

        createUser(user,password);
    }

    @Test
    public void testAttendEvent(){

        String userId = "user";
        String password = "password";
        createUser(userId,password);

        Long eventId = eventEjb.createEvent("title","text","Norway","location",userId);
        assertFalse(userEJB.isUserAttendingEvent(userId, eventId));

        userEJB.addEvent(userId, eventId);
        assertTrue(userEJB.isUserAttendingEvent(userId, eventId));

        userEJB.removeEvent(userId, eventId);
        assertFalse(userEJB.isUserAttendingEvent(userId, eventId));
    }




    @Test
    public void testNoTwoUsersWithSameId(){

        String user = "user";

        boolean created = createUser(user,"a");
        assertTrue(created);

        created = createUser(user,"b");
        assertFalse(created);
    }

    @Test
    public void testSamePasswordLeadToDifferentHashAndSalt(){

        String password = "password";
        String first = "first";
        String second = "second";

        createUser(first,password);
        createUser(second,password); //same password

        User f = userEJB.getUser(first);
        User s = userEJB.getUser(second);

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

        createUser(user, correct);

        boolean  canLogin = userEJB.login(user, correct);
        assertTrue(canLogin);

        canLogin = userEJB.login(user, wrong);
        assertFalse(canLogin);
    }

    @Test
    public void testBeSurePasswordIsNotStoredInPlain(){

        String user = "user";
        String password = "password";

        createUser(user, password);

        User entity = userEJB.getUser(user);
        assertNotEquals(password, entity.getUserId());
        assertNotEquals(password, entity.getHash());
        assertNotEquals(password, entity.getSalt());
    }

}