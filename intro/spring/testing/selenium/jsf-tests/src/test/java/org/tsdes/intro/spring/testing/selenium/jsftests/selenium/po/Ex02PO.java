package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.tsdes.misc.testutils.selenium.PageObject;

public class Ex02PO  extends TemplatePO{

    public Ex02PO(PageObject po) {
        super(po);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Base layout example");
    }
}
