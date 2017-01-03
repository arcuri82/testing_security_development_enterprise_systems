package org.tsdes.jee.jsf.examples.ex01;

import org.junit.Before;
import org.junit.Test;
import org.tsdes.jee.jsf.examples.test.SeleniumTestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CounterBeanIT extends SeleniumTestBase {

    private CounterPageObject po;

    @Before
    public void startFromInitialPage() {

        po = new CounterPageObject(getDriver());
        po.toStartingPage();
        assertTrue(po.isOnPage());
        assertEquals(Integer.valueOf(0), po.getCounterValue());
    }


    @Test
    public void testIncr() {
        int x = po.getCounterValue();
        po.clickPlus();
        int res = po.getCounterValue();
        assertEquals(x + 1, res);
    }

    @Test
    public void testFailedDecr() {
        po.clickReset();
        int res = po.getCounterValue();
        assertEquals(0, res);
        po.clickMinus();
        res = po.getCounterValue();
        assertEquals(0, res);
    }

    @Test
    public void testDecr() {
        int x = po.getCounterValue();
        po.clickPlus();
        assertEquals(x + 1, (int) po.getCounterValue());
        po.clickMinus();
        assertEquals(x, (int) po.getCounterValue());
    }

    @Test
    public void testIncrAndDecr() {
        po.clickReset(); //0
        po.clickMinus(); //no effect
        po.clickPlus();  // 1
        po.clickPlus();  // 2
        po.clickMinus(); // 1
        po.clickPlus();  // 2
        assertEquals(2, (int) po.getCounterValue());
    }
}
