package org.tsdes.intro.spring.testing.selenium.jsftests.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.spring.jsf.Application;
import org.tsdes.intro.spring.jsf.ex03.Comment;
import org.tsdes.intro.spring.jsf.ex03.CommentService;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, DeleterService.class},
        webEnvironment = NONE)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DeleterService deleterService;

    @Before
    @After
    public void clearDatabase(){
        deleterService.deleteEntities(Comment.class);
    }

    @Test
    public void testCreate(){

        Assert.assertEquals(0 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("Hello");

        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(1 , comments.size());
        assertTrue(comments.stream().map(Comment::getText).anyMatch(s -> "Hello".equals(s)));

        commentService.createNewComment("World");

        comments = commentService.getMostRecentComments(10);
        assertEquals(2 , comments.size());
        assertTrue(comments.stream().map(Comment::getText).anyMatch(s -> "Hello".equals(s)));
        assertTrue(comments.stream().map(Comment::getText).anyMatch(s -> "World".equals(s)));

        /*
            Note: instead of using comments.stream(), I could have used comments.get(),
            as the values are supposed to be sorted.
            However, a test should not test too much... I mean, here we want to test the
            feature/requirement of being able to create and retrieve new comments.
            The fact that they should be sorted is a further feature/requirement that
            is best to test with a further, independent test.
         */
    }

    @Test
    public void testReturnDatesInOrder(){

        commentService.createNewComment("a");
        commentService.createNewComment("b");
        commentService.createNewComment("c");

        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(3 , comments.size());

        /*
            not: these check might fail if one day computers are so fast to do 2
            createNewComment in the same millisecond. However, in such cases
            could just add some artificial Thread.sleep
         */
        assertTrue(comments.get(0).getDate().compareTo(comments.get(1).getDate()) > 0);
        assertTrue(comments.get(1).getDate().compareTo(comments.get(2).getDate()) > 0);

        /*
            Note: here I am not really testing that what I insert (ie, "a", "b" and "c")
            is really what I get back.
            I could add extra assertions for it, but then it would make the test longer
            and more difficult to understand.
            As a rule of thumbs, tests should be short and to the point.
            If you need to tests several different behaviors/features, just write more tests
            instead of a having a long one
         */
    }

    @Test
    public void testReturnValuesInOrder() throws Exception{

        commentService.createNewComment("a");
        Thread.sleep(10);
        commentService.createNewComment("b");
        Thread.sleep(10);
        commentService.createNewComment("c");
        Thread.sleep(10);
        commentService.createNewComment("d");
        Thread.sleep(10);

        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(4 , comments.size());

        Assert.assertEquals("d", comments.get(0).getText());
        Assert.assertEquals("c", comments.get(1).getText());
        Assert.assertEquals("b", comments.get(2).getText());
        Assert.assertEquals("a", comments.get(3).getText());
    }

    @Test
    public void testAll(){

        /*
            This is putting together the previous tests.
            The length of this test is shorter than the sum of those tests,
            but it is arguably more difficult to understand and maintain.

            This test has _12_ assertions on 4 different features/requirements...
            which can be considered too many.

            Note: the test is not so long, ie just 17 statements, but still it
            is testing too much.

            Note 2: this is true for unit tests, which only take few ms/ns to run.
            For expensive system tests, things might be slightly different (although
            the same idea of trying to have "short tests to the point" would still apply)
         */

        Assert.assertEquals(0 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("a");
        Assert.assertEquals(1 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("b");
        Assert.assertEquals(2 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("c");
        Assert.assertEquals(3 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("d");
        Assert.assertEquals(4 , commentService.getMostRecentComments(10).size());


        List<Comment> comments = commentService.getMostRecentComments(10);

        Assert.assertEquals("d", comments.get(0).getText());
        Assert.assertEquals("c", comments.get(1).getText());
        Assert.assertEquals("b", comments.get(2).getText());
        Assert.assertEquals("a", comments.get(3).getText());

        assertTrue(comments.get(0).getDate().compareTo(comments.get(1).getDate()) > 0);
        assertTrue(comments.get(1).getDate().compareTo(comments.get(2).getDate()) > 0);
        assertTrue(comments.get(2).getDate().compareTo(comments.get(3).getDate()) > 0);
    }


    @Test
    public void testCreateFail(){

        try{
            commentService.createNewComment("");
            fail();
        } catch (Exception e){}

        try{
            commentService.createNewComment(new String(new char[1000]));
            fail();
        } catch (Exception e){}

    }

    @Test
    public void testReturnLimits(){

        commentService.createNewComment("a");
        commentService.createNewComment("b");
        commentService.createNewComment("c");


        try{
            commentService.getMostRecentComments(-1);
            fail();
        } catch (Exception e){}


        Assert.assertEquals(0 , commentService.getMostRecentComments(0).size());
        Assert.assertEquals(1 , commentService.getMostRecentComments(1).size());
        Assert.assertEquals(2 , commentService.getMostRecentComments(2).size());
        Assert.assertEquals(3 , commentService.getMostRecentComments(3).size());

        //checking when asking more than available
        Assert.assertEquals(3 , commentService.getMostRecentComments(4).size());
        Assert.assertEquals(3 , commentService.getMostRecentComments(100).size());
    }


    @Test
    public void testDelete(){

        commentService.createNewComment("a");
        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(1 , comments.size());

        Long id = comments.get(0).getId();
        commentService.deleteComment(id);

        Assert.assertEquals(0 , commentService.getMostRecentComments(10).size());
    }

}