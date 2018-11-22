package org.tsdes.intro.spring.testing.selenium.jsftests.selenium;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public abstract class SeleniumTestBase {

    protected abstract WebDriver getDriver();

    protected abstract String getServerHost();

    protected abstract int getServerPort();


    private HomePO home;

    @BeforeEach
    public void initTest(){
        home = new HomePO(getDriver(), getServerHost(), getServerPort());

        home.toStartingPage();

        assertTrue(home.isOnPage(), "Failed to start from Home Page");
    }





    //--- Ex 00 ----------------------------------------------------------------

    @Test
    public void testEx00Date(){

        Ex00PO po = home.toEx00Page();
        LocalDate date = po.getDate();

        assertTrue(date.equals(LocalDate.now()));
    }

    //--- Ex 01 ----------------------------------------------------------------

    @Test
    public void testEx01Incr() {
        Ex01PO po = home.toEx01Page();

        int x = po.getCounterValue();
        po.clickPlus();
        int res = po.getCounterValue();
        assertEquals(x + 1, res);
    }

    @Test
    public void testEx01FailedDecr() {
        Ex01PO po = home.toEx01Page();

        po.clickReset();
        int res = po.getCounterValue();
        assertEquals(0, res);
        po.clickMinus();
        res = po.getCounterValue();
        assertEquals(0, res);
    }

    @Test
    public void testEx01Decr() {
        Ex01PO po = home.toEx01Page();

        int x = po.getCounterValue();
        po.clickPlus();
        assertEquals(x + 1, (int) po.getCounterValue());
        po.clickMinus();
        assertEquals(x, (int) po.getCounterValue());
    }

    @Test
    public void testEx01IncrAndDecr() {
        Ex01PO po = home.toEx01Page();

        po.clickReset(); //0
        po.clickMinus(); //no effect
        po.clickPlus();  // 1
        po.clickPlus();  // 2
        po.clickMinus(); // 1
        po.clickPlus();  // 2
        assertEquals(2, (int) po.getCounterValue());
    }

    //--- Ex 02 ----------------------------------------------------------------

    @Test
    public void testEx02HomeLink(){

        assertTrue(home.isOnPage());

        Ex02PO ex02 = home.toEx02Page();
        assertFalse(home.isOnPage());
        assertTrue(ex02.isOnPage());

        ex02.clickBackHome();
        assertTrue(home.isOnPage());
        assertFalse(ex02.isOnPage());

        /*
            could write the same kind of tests for the other links as well,
            but not the first 2 (ex00 and ex01), as they do not use the template
            with the Home link having an id
         */
    }


    //--- Ex 03 ----------------------------------------------------------------


    private final int MAX = 10; // the max value of comments in the table

    @Test
    public void testEx03Create(){
        Ex03PO po = home.toEx03Page();

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
    public void testEx03Delete(){
        Ex03PO po = home.toEx03Page();

        String baseText = "testDelete() ";

        //fill table with new comments
        for(int i=0; i<MAX; i++) {
            int index = (MAX-1) - i; //want to insert 9,8,7,..,0 so that 0,1,2,... is on top
            po.createNewComment(baseText + index);
        }

        //just make sure first and last are correct
        assertEquals(0, po.extractIndex(0));
        assertEquals(9, po.extractIndex(9));

        po.deleteComment(0);
        //all should have shift position
        assertEquals(1, po.extractIndex(0));
        assertEquals(9, po.extractIndex(8));

        po.deleteComment(1);
        assertEquals(1, po.extractIndex(0)); // this should had stayed the same, as removing after it
        assertEquals(3, po.extractIndex(1));
        assertEquals(9, po.extractIndex(7));
    }



    //--- Ex 04 ----------------------------------------------------------------

    @Test
    public void testEx04Request(){

        Ex04PO po = home.toEx04Page();

        po.clickRequestReset();
        assertEquals(0, po.getRequestCounter());

        po.clickRequestPlus();
        assertEquals(1, po.getRequestCounter());

        //not changing
        po.clickRequestPlus();
        assertEquals(1, po.getRequestCounter());

        po.clickRequestPlus();
        assertEquals(1, po.getRequestCounter());

        //calling other on Session will reset the one for Request
        po.clickSessionPlus();
        assertEquals(0, po.getRequestCounter());
    }

    @Test
    public void testEx04Session(){

        Ex04PO po = home.toEx04Page();

        po.clickSessionReset();
        assertEquals(0, po.getSessionCounter());

        po.clickSessionPlus();
        assertEquals(1, po.getSessionCounter());

        po.clickSessionPlus();
        assertEquals(2, po.getSessionCounter());

        po.clickSessionPlus();
        assertEquals(3, po.getSessionCounter());

        po.clickSessionMinus();
        assertEquals(2, po.getSessionCounter());

        //calling on Request has no effect for Session
        po.clickRequestPlus();
        assertEquals(2, po.getSessionCounter());
    }

    //--- Ex 05 ----------------------------------------------------------------

    @Test
    public void testEx05Params(){

        Ex05MainPO main = home.toEx05Page();

        Ex05ParamsPO params = main.clickOnNoParamsLink();
        assertEquals(0, params.getCounterOfParams());

        main = params.clickOnBackToMain();
        params = main.clickOnOneParamLink();
        assertEquals(1, params.getCounterOfParams());

        main = params.clickOnBackToMain();
        params = main.clickOnParamNoValueLink();
        assertEquals(1, params.getCounterOfParams());

        main = params.clickOnBackToMain();
        params = main.clickOnMultiParamsLink();
        assertEquals(3, params.getCounterOfParams());
    }

    //--- Ex 06 ----------------------------------------------------------------

    @Test
    public void testEx06Redirection(){

        Ex06MainPO main = home.toEx06Page();
        Ex06ResultPO result = main.clickForward();

        int x = result.getDisplayedCounter();

        main = result.clickBackToMain();
        result = main.clickForward();
        assertEquals(x+1, result.getDisplayedCounter());

        main = result.clickBackToMain();
        result = main.clickRedirect();
        assertEquals(x+2, result.getDisplayedCounter());

        result.refresh();
        //no change
        assertEquals(x+2, result.getDisplayedCounter());


        main = result.clickBackToMain();
        result = main.clickForward();
        assertEquals(x+3, result.getDisplayedCounter());

        result.refresh();
        //the POST has been repeated, increasing the counter
        assertEquals(x+4, result.getDisplayedCounter());
    }
}
