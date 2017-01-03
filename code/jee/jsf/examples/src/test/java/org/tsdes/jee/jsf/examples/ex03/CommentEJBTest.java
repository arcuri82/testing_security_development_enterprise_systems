package org.tsdes.jee.jsf.examples.ex03;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.jee.jsf.examples.test.DeleterEJB;

import javax.ejb.EJB;


import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CommentEJBTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(CommentEJB.class, Comment.class, DeleterEJB.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private CommentEJB ejb;
    @EJB
    private DeleterEJB deleterEJB;

    @Before
    @After
    public void clearDatabase(){
        deleterEJB.deleteEntities(Comment.class);
    }

    @Test
    public void testCreate(){

        assertEquals(0 , ejb.getMostRecentComments(10).size());

        ejb.createNewComment("Hello");

        List<Comment> comments = ejb.getMostRecentComments(10);
        assertEquals(1 , comments.size());
        assertTrue(comments.stream().map(Comment::getText).anyMatch(s -> "Hello".equals(s)));

        ejb.createNewComment("World");

        comments = ejb.getMostRecentComments(10);
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

        ejb.createNewComment("a");
        ejb.createNewComment("b");
        ejb.createNewComment("c");

        List<Comment> comments = ejb.getMostRecentComments(10);
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
    public void testReturnValuesInOrder(){

        ejb.createNewComment("a");
        ejb.createNewComment("b");
        ejb.createNewComment("c");
        ejb.createNewComment("d");

        List<Comment> comments = ejb.getMostRecentComments(10);
        assertEquals(4 , comments.size());

        assertEquals("d", comments.get(0).getText());
        assertEquals("c", comments.get(1).getText());
        assertEquals("b", comments.get(2).getText());
        assertEquals("a", comments.get(3).getText());
    }

    @Test
    public void aBadTestThatYouShould_NOT_write(){

        /*
            This is putting together the previous tests.
            The length of this test is shorter than the sum of those tests,
            but it is arguably more difficult to understand and maintain.

            This test has _12_ assertions on 4 different features/requirements...
            far too many.
            Note: the test is not so long, ie just 17 statements, but still it
            is testing too much.

            Note 2: this is true for unit tests, which only take few ms/ns to run.
            For expensive system tests, things might be slightly different (although
            the same idea of trying to have "short tests to the point" would still apply)
         */

        assertEquals(0 , ejb.getMostRecentComments(10).size());

        ejb.createNewComment("a");
        assertEquals(1 , ejb.getMostRecentComments(10).size());

        ejb.createNewComment("b");
        assertEquals(2 , ejb.getMostRecentComments(10).size());

        ejb.createNewComment("c");
        assertEquals(3 , ejb.getMostRecentComments(10).size());

        ejb.createNewComment("d");
        assertEquals(4 , ejb.getMostRecentComments(10).size());


        List<Comment> comments = ejb.getMostRecentComments(10);

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
            ejb.createNewComment("");
            fail();
        } catch (Exception e){}

        try{
            ejb.createNewComment(new String(new char[1000]));
            fail();
        } catch (Exception e){}

    }

    @Test
    public void testReturnLimits(){

        ejb.createNewComment("a");
        ejb.createNewComment("b");
        ejb.createNewComment("c");


        try{
            ejb.getMostRecentComments(-1);
            fail();
        } catch (Exception e){}

        /*
            WARN: undocumented, counter-intuitive behavior:
            if 0 value, JPA/Hibernate just ignores it and returns everything.
            Not that a query with 0 results would make sense, but if passing
            0 by mistake (ie bug), it would be hard to find out
         */
        assertEquals(3 , ejb.getMostRecentComments(0).size());

        assertEquals(1 , ejb.getMostRecentComments(1).size());
        assertEquals(2 , ejb.getMostRecentComments(2).size());
        assertEquals(3 , ejb.getMostRecentComments(3).size());

        //checking when asking more than available
        assertEquals(3 , ejb.getMostRecentComments(4).size());
        assertEquals(3 , ejb.getMostRecentComments(100).size());
    }


    @Test
    public void testDelete(){

        ejb.createNewComment("a");
        List<Comment> comments = ejb.getMostRecentComments(10);
        assertEquals(1 , comments.size());

        Long id = comments.get(0).getId();
        ejb.deleteComment(id);

        assertEquals(0 , ejb.getMostRecentComments(10).size());
    }

}