package org.tsdes.intro.spring.testing.selenium.jsftests.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.jsf.Application;
import org.tsdes.intro.spring.jsf.ex03.Comment;
import org.tsdes.intro.spring.jsf.ex03.CommentService;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class, DeleterService.class},
        webEnvironment = NONE)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DeleterService deleterService;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        deleterService.deleteEntities(Comment.class);
    }

    @Test
    public void testCreate(){

        assertEquals(0 , commentService.getMostRecentComments(10).size());

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
    public void testReturnDatesInOrder() throws Exception {

        commentService.createNewComment("a");
        Thread.sleep(1);
        commentService.createNewComment("b");
        Thread.sleep(1);
        commentService.createNewComment("c");

        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(3 , comments.size());


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

        assertEquals("d", comments.get(0).getText());
        assertEquals("c", comments.get(1).getText());
        assertEquals("b", comments.get(2).getText());
        assertEquals("a", comments.get(3).getText());
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

        assertEquals(0 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("a");
        assertEquals(1 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("b");
        assertEquals(2 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("c");
        assertEquals(3 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("d");
        assertEquals(4 , commentService.getMostRecentComments(10).size());


        List<Comment> comments = commentService.getMostRecentComments(10);

        assertEquals("d", comments.get(0).getText());
        assertEquals("c", comments.get(1).getText());
        assertEquals("b", comments.get(2).getText());
        assertEquals("a", comments.get(3).getText());

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


        assertEquals(0 , commentService.getMostRecentComments(0).size());
        assertEquals(1 , commentService.getMostRecentComments(1).size());
        assertEquals(2 , commentService.getMostRecentComments(2).size());
        assertEquals(3 , commentService.getMostRecentComments(3).size());

        //checking when asking more than available
        assertEquals(3 , commentService.getMostRecentComments(4).size());
        assertEquals(3 , commentService.getMostRecentComments(100).size());
    }


    @Test
    public void testDelete(){

        commentService.createNewComment("a");
        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(1 , comments.size());

        Long id = comments.get(0).getId();
        commentService.deleteComment(id);

        assertEquals(0 , commentService.getMostRecentComments(10).size());
    }

}