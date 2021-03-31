package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.openqa.selenium.WebDriver;
import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

/**
 * Created by arcuri82 on 02-Feb-18.
 */
public class HomePO extends PageObject{

    public HomePO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public HomePO(PageObject po) {
        this(po.getDriver(), po.getHost(), po.getPort());
    }

    public void toStartingPage(){
        toOrigin();
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("JSF Examples");
    }

    public Ex00PO toEx00Page(){

        clickAndWait("ex00LinkId");
        Ex00PO po = new Ex00PO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex01PO toEx01Page(){

        clickAndWait("ex01LinkId");
        Ex01PO po = new Ex01PO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex02PO toEx02Page(){

        clickAndWait("ex02LinkId");
        Ex02PO po = new Ex02PO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex03PO toEx03Page(){

        clickAndWait("ex03LinkId");
        Ex03PO po = new Ex03PO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex04PO toEx04Page(){

        clickAndWait("ex04LinkId");
        Ex04PO po = new Ex04PO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex05MainPO toEx05Page(){

        clickAndWait("ex05LinkId");
        Ex05MainPO po = new Ex05MainPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex06MainPO toEx06Page(){

        clickAndWait("ex06LinkId");
        Ex06MainPO po = new Ex06MainPO(this);
        assertTrue(po.isOnPage());

        return po;
    }
}
