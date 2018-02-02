package org.tsdes.misc.testutils.selenium;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by arcuri82 on 28-Nov-17.
 */
public abstract class SeleniumTestBase {

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    protected static String getUniqueId() {
        return "foo" + counter.incrementAndGet();
    }

    protected abstract WebDriver getDriver();

    protected abstract String getServerHost();

    protected abstract int getServerPort();

}
