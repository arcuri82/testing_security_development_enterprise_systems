package org.tsdes.jee.exercises.mynews.backend.ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.jee.exercises.mynews.backend.entity.Post;

import javax.ejb.EJBException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class PostEJBTest extends EjbTestBase{

    @Test
    public void testCreatePost(){

        String text = "some text";

        String userId = "foo";
        createUser(userId);

        assertEquals(0 , postEJB.getAllPostsByScore().size());

        long id = postEJB.createPost(userId, text);

        assertEquals(1 , postEJB.getAllPostsByScore().size());

        assertEquals(text, postEJB.getPost(id).getText());
    }


    @Test
    public void testVoteFor(){

        String a = "a", b = "b", c = "c";

        createUser(a);
        createUser(b);
        createUser(c);
        long id = postEJB.createPost(a, "some text");


        postEJB.voteFor(a, id);
        postEJB.voteFor(b, id);
        postEJB.voteFor(c, id);

        assertEquals(3, postEJB.getPost(id).computeScore());
    }

    @Test
    public void testVoteAgainst(){

        String a = "a", b = "b", c = "c";

        createUser(a);
        createUser(b);
        createUser(c);
        long id = postEJB.createPost(a, "some text");


        postEJB.voteAgainst(a, id);
        postEJB.voteAgainst(b, id);
        postEJB.voteAgainst(c, id);

        assertEquals(-3, postEJB.getPost(id).computeScore());
    }

    @Test
    public void testVote(){

        String a = "a", b = "b", c = "c";

        createUser(a);
        createUser(b);
        createUser(c);
        long id = postEJB.createPost(a, "some text");


        postEJB.voteAgainst(a, id);
        postEJB.voteAgainst(b, id);
        postEJB.voteFor(c, id);

        assertEquals(-1, postEJB.getPost(id).computeScore());
    }

    @Test(expected = EJBException.class)
    public void testCannotVoteForTwice(){

        String userId = "foo";
        createUser(userId);
        long id = postEJB.createPost(userId, "some text");

        postEJB.voteFor(userId, id);
        postEJB.voteFor(userId, id);
    }

    @Test(expected = EJBException.class)
    public void testCannotVoteAgainstTwice(){

        String userId = "foo";
        createUser(userId);
        long id = postEJB.createPost(userId, "some text");

        postEJB.voteAgainst(userId, id);
        postEJB.voteAgainst(userId, id);
    }

    @Test
    public void testUnvote(){
        String userId = "foo";
        createUser(userId);
        long id = postEJB.createPost(userId, "some text");

        postEJB.voteFor(userId, id);
        assertEquals(1, postEJB.getPost(id).computeScore());

        postEJB.noVote(userId, id);
        assertEquals(0, postEJB.getPost(id).computeScore());
    }

    @Test
    public void testChangeVote(){

        String userId = "foo";
        createUser(userId);
        long id = postEJB.createPost(userId, "some text");

        postEJB.voteFor(userId, id);
        assertEquals(1, postEJB.getPost(id).computeScore());

        postEJB.voteAgainst(userId, id);
        assertEquals(-1, postEJB.getPost(id).computeScore());

        postEJB.voteFor(userId, id);
        assertEquals(1, postEJB.getPost(id).computeScore());
    }

    @Test
    public void testGetAllPostByTime(){

        String userId = "foo";
        createUser(userId);
        long a = postEJB.createPost(userId, "some text a");
        long b = postEJB.createPost(userId, "some text b");
        long c = postEJB.createPost(userId, "some text c");
        postEJB.createComment(userId, a, "a comment");

        List<Post> posts = postEJB.getAllPostsByTime();
        assertEquals(3, posts.size());

        assertEquals(c, (long) posts.get(0).getId());
        assertEquals(b, (long) posts.get(1).getId());
        assertEquals(a, (long) posts.get(2).getId());
    }

    @Test
    public void testGetAllPostByTimeWithVotes(){

        String a = "a", b = "b", c = "c";

        createUser(a);
        createUser(b);
        createUser(c);

        List<Post> posts = postEJB.getAllPostsByTime();
        assertEquals(0, posts.size());

        long id = postEJB.createPost(a, "some text");

        postEJB.voteAgainst(a, id);
        postEJB.voteAgainst(b, id);
        postEJB.voteFor(c, id);

        postEJB.getPost(id).computeScore();

        posts = postEJB.getAllPostsByTime();
        assertEquals(1, posts.size());
    }


    @Test
    public void testGetAllPostByScore(){

        String userId = "foo";
        createUser(userId);
        long a = postEJB.createPost(userId, "some text a");
        long b = postEJB.createPost(userId, "some text b");
        long c = postEJB.createPost(userId, "some text c");
        postEJB.createComment(userId, a, "a comment");

        String anotherUser = "anotherUser";
        createUser(anotherUser);

        //0
        postEJB.voteFor(userId, a);
        postEJB.voteAgainst(anotherUser, a);

        //2
        postEJB.voteFor(userId, b);
        postEJB.voteFor(anotherUser, b);

        //-1
        postEJB.voteAgainst(userId, c);

        List<Post> posts = postEJB.getAllPostsByScore();
        assertEquals(3, posts.size());

        assertEquals(b, (long) posts.get(0).getId());
        assertEquals(a, (long) posts.get(1).getId());
        assertEquals(c, (long) posts.get(2).getId());
    }

    // comments -----------------------------

    @Test
    public void testCreateComment(){

        String usedId = "foo";
        createUser(usedId);
        long postId = postEJB.createPost(usedId, "text");

        postEJB.createComment(usedId, postId, "a comment");
        postEJB.createComment(usedId, postId, "another comment");

        Post post = postEJB.getPost(postId);
        assertEquals(2, post.getComments().size());
    }

    @Test
    public void testModerateOwn(){

        String usedId = "foo";
        createUser(usedId);
        long postId = postEJB.createPost(usedId, "text");

        long commentID = postEJB.createComment(usedId, postId, "a comment");

        assertFalse(postEJB.getPost(postId).getComments().get(0).getModerated());

        postEJB.moderate(usedId, commentID, true);
        assertTrue(postEJB.getPost(postId).getComments().get(0).getModerated());

        postEJB.moderate(usedId, commentID, false);
        assertFalse(postEJB.getPost(postId).getComments().get(0).getModerated());
    }

    @Test(expected = EJBException.class)
    public void testFailModerateOther(){

        String usedId = "foo";
        createUser(usedId);
        long postId = postEJB.createPost(usedId, "text");

        long commentID = postEJB.createComment(usedId, postId, "a comment");

        String anotherUser = "another";
        createUser(anotherUser);
        postEJB.moderate(anotherUser, commentID, true);
    }

    @Test
    public void testVoteForComment(){

        String usedId = "foo";
        createUser(usedId);
        long postId = postEJB.createPost(usedId, "text");
        long commentId = postEJB.createComment(usedId, postId, "a comment");

        postEJB.voteFor(usedId, commentId);

        Post post = postEJB.getPost(postId);

        assertEquals(1 , post.getComments().get(0).computeScore());
        assertEquals(0 , post.computeScore());
    }
}