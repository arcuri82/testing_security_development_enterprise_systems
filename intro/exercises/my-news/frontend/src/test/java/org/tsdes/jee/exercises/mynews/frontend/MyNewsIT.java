package org.tsdes.jee.exercises.mynews.frontend;

import org.junit.Test;
import org.tsdes.jee.exercises.mynews.frontend.po.CommentsPageObject;
import org.tsdes.jee.exercises.mynews.frontend.po.UserDetailsPageObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyNewsIT  extends WebTestBase{

    @Test
    public void testCreateNews(){
        String userId = getUniqueId();

        createAndLogNewUser(userId);

        assertEquals(0, home.getNumberOfDiplayedPostsMadeByAUser(userId));

        home.createNewEvent("some text");

        assertEquals(1, home.getNumberOfDiplayedPostsMadeByAUser(userId));

        home.createNewEvent("some more text");

        assertEquals(2, home.getNumberOfDiplayedPostsMadeByAUser(userId));

    }

    @Test
    public void testNewsAfterLogout(){
        String userId = getUniqueId();

        createAndLogNewUser(userId);
        home.createNewEvent("some text");
        home.createNewEvent("some more text");

        assertEquals(2, home.getNumberOfDiplayedPostsMadeByAUser(userId));

        home.logout();

        assertEquals(2, home.getNumberOfDiplayedPostsMadeByAUser(userId));
    }

    @Test
    public void testUserDetails(){
        String userId = getUniqueId();
        createAndLogNewUser(userId);
        home.createNewEvent("some text");

        UserDetailsPageObject details = home.toUserDetails(userId);
        assertTrue(details.isOnPage());

        assertEquals(userId, details.getUserId());
    }


    @Test
    public void testCanVote(){
        String userId = getUniqueId();
        createAndLogNewUser(userId);
        home.createNewEvent("some text");

        assertTrue(home.isVotingVisible());
        home.logout();
        assertFalse(home.isVotingVisible());

        loginExistingUser(userId);
        assertTrue(home.isVotingVisible());
    }


    @Test
    public void testScore(){

        String userId = getUniqueId();
        createAndLogNewUser(userId);
        home.createNewEvent("some text");

        home.sortPostsBy(PostController.Sorting.Time);
        int position = 0;

        assertEquals(0, home.getScore(position));

        home.voteFor(position);
        assertEquals(1, home.getScore(position));

        home.voteAgainst(position);
        assertEquals(-1, home.getScore(position));

        home.voteNone(position);
        assertEquals(0, home.getScore(position));
    }

    @Test
    public void testScoreWithTwoUsers(){

        createAndLogNewUser(getUniqueId());
        home.createNewEvent("some text");

        home.sortPostsBy(PostController.Sorting.Time);
        int position = 0;

        home.voteFor(position);
        assertEquals(1, home.getScore(position));

        home.logout();
        createAndLogNewUser(getUniqueId());
        home.voteFor(position);
        assertEquals(2, home.getScore(position));
    }

    @Test
    public void testLongText(){

        String prefix = "Long text ";
        assertEquals(10, prefix.length());
        String text = prefix + "01234567890123456789012345678901234567890123456789";
        assertTrue(text.length() > 30);

        createAndLogNewUser(getUniqueId());
        home.createNewEvent(text);

        home.sortPostsBy(PostController.Sorting.Time);
        int position = 0;

        String res = home.getPostText(position);
        assertEquals("Res: "+ res, 30, res.length());
        assertTrue("Wrong result: "+res, res.startsWith(prefix));
        assertTrue("Wrong result: "+res, res.endsWith("..."));
    }


    @Test
    public void testSorting(){

        createAndLogNewUser(getUniqueId());
        home.createNewEvent("some text");

        home.sortPostsBy(PostController.Sorting.Time);

        int position = 0;
        home.voteFor(position);

        home.createNewEvent("some text");
        assertFalse(home.arePostsSortedByScore());

        home.sortPostsBy(PostController.Sorting.Score);
        assertTrue(home.arePostsSortedByScore());

        home.sortPostsBy(PostController.Sorting.Time);
        assertFalse(home.arePostsSortedByScore());
    }

    @Test
    public void testCreateComment(){

        createAndLogNewUser(getUniqueId());
        home.createNewEvent("some text");
        CommentsPageObject cpo = home.openNews(0);
        assertTrue(cpo.isOnPage());

        assertEquals(0, cpo.getNumberOfComments());

        cpo.createComment("foo");
        cpo.createComment("foo");
        cpo.createComment("foo");

        assertEquals(3, cpo.getNumberOfComments());
    }

    @Test
    public void testCanModerate(){

        String userId = getUniqueId();
        createAndLogNewUser(userId);
        home.createNewEvent("some text");

        CommentsPageObject cpo = home.openNews(0);
        cpo.createComment("foo");
        assertTrue(cpo.isModerateVisible());

        home.logout();
        cpo = home.openNews(0);
        assertFalse(cpo.isModerateVisible());

        createAndLogNewUser(getUniqueId());
        cpo = home.openNews(0);
        assertFalse(cpo.isModerateVisible());


        loginExistingUser(userId);
        cpo = home.openNews(0);
        assertTrue(cpo.isModerateVisible());
    }


    @Test
    public void testKarma(){

        String userId = getUniqueId();
        createAndLogNewUser(userId);
        home.createNewEvent("some text");

        home.voteFor(0);

        CommentsPageObject cpo = home.openNews(0);
        cpo.createComment("a");
        cpo.createComment("b");
        cpo.doModerate(0,true);
        cpo.doModerate(1,true);

        cpo.toHomePage();
        UserDetailsPageObject details = home.toUserDetails(userId);

        assertEquals(-19, details.getKarma());
    }
}
