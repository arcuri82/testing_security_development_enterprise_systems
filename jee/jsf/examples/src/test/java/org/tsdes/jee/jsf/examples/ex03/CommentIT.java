package org.tsdes.jee.jsf.examples.ex03;

import org.junit.Before;
import org.junit.Test;
import org.tsdes.jee.jsf.examples.test.SeleniumTestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommentIT extends SeleniumTestBase{

    private final int MAX = 10; // the max value of comments in the table

    private CommentPageObject po;

    @Before
    public void startFromInitialPage(){
        po = new CommentPageObject(getDriver());
        po.toStartingPage();
        assertTrue(po.isOnPage());
    }

    /*
        Recall, for this kind of tests were the web application is started just once,
        when I run a test I cannot make assumptions on the current state (eg databases
        and Singleton EJBs).
        So, to still make the tests "independent", need to write them in a way in
        which they should be valid regardless of the current state of the application.
     */

    @Test
    public void testCreate(){

        String text = "testCreate() foo"; //just use something unique for this test

        int n = po.getNumberOfComments(); //cannot make assumptions on "n" at this point
        po.createNewComment(text);

        int k = po.getNumberOfComments();

        //either we already had the maximum, or should had been increased by 1
        if(n==MAX){
            assertEquals(n , k);
        } else {
            assertEquals(n+1, k);
        }

        //new comment should always be on top, in position 0
        assertEquals(text, po.getCommentText(0));
    }


    @Test
    public void testDelete(){

        String baseText = "testDelete() ";

        //fill table with new comments
        for(int i=0; i<MAX; i++) {
            int index = (MAX-1) - i; //want to insert 9,8,7,..,0 so that 0,1,2,... is on top
            po.createNewComment(baseText + index);
        }

        //just make sure first and last are correct
        assertEquals(0, extractIndex(0));
        assertEquals(9, extractIndex(9));

        po.deleteComment(0);
        //all should have shift position
        assertEquals(1, extractIndex(0));
        assertEquals(9, extractIndex(8));

        po.deleteComment(1);
        assertEquals(1, extractIndex(0)); // this should had stayed the same, as removing after it
        assertEquals(3, extractIndex(1));
        assertEquals(9, extractIndex(7));
    }

    private int extractIndex(int position){
        String comment = po.getCommentText(position);
        return Integer.parseInt(comment.split(" ")[1]);
    }
}
