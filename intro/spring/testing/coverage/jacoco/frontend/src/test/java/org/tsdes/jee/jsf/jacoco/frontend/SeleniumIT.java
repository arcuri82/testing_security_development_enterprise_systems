package org.tsdes.jee.jsf.jacoco.frontend;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SeleniumIT extends SeleniumTestBase{

    private HomePageObject po;

    @Before
    public void startFromInitialPage() {

        po = new HomePageObject(getDriver());
        po.toStartingPage();
        assertTrue(po.isOnPage());
    }

    @Test
    public void testChangeData(){

        String first = "foo data";
        po.changeData(first);
        assertTrue(getDriver().getPageSource().contains(first));

        String second = "bar data";
        po.changeData(second);
        String src = getDriver().getPageSource();
        assertTrue(src.contains(second));
        assertFalse(src.contains(first));
    }
}