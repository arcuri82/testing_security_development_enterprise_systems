package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.tsdes.misc.testutils.selenium.PageObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ex00PO extends PageObject {

    public Ex00PO(PageObject po) {
        super(po);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Example of Dynamic Page");
    }

    public LocalDate getDate() {
        return LocalDate.parse(getText("dateTextId"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
}
