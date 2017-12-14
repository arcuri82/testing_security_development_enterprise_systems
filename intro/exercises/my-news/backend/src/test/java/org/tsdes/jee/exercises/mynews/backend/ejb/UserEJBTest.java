package org.tsdes.jee.exercises.mynews.backend.ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.jee.exercises.mynews.backend.entity.User;
import javax.ejb.EJBException;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest extends EjbTestBase{


    @Test
    public void testKarmaWithModeration(){

        String usedId = "foo";
        createUser(usedId);
        long postId = postEJB.createPost(usedId, "text");
        long commentId = postEJB.createComment(usedId, postId, "a comment");
        long secondCommentId = postEJB.createComment(usedId, postId, "yet another comment");
        long thirdCommentId = postEJB.createComment(usedId, postId, "yet a third comment");
        long fourthCommentId = postEJB.createComment(usedId, postId, "yet a fourth comment");

        String anotherUser = "anotherUser";
        createUser(anotherUser);

        postEJB.voteAgainst(usedId, postId);  // -1

        postEJB.voteFor(usedId, commentId);
        postEJB.voteFor(anotherUser, commentId); // + 2

        postEJB.moderate(usedId, secondCommentId, true); // -10

        postEJB.voteFor(usedId, thirdCommentId); // +1

        postEJB.moderate(usedId, fourthCommentId, true); // -10

        //-1 + 2 + (-10) + 1 + (-10) =  -18
        assertEquals(-18, userEJB.computeKarma(usedId));
    }

    @Test
    public void testCanCreateAUser(){

        String user = "user";
        String password = "password";

        boolean created = createUser(user,password);
        assertTrue(created);
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