package org.tsdes.intro.jee.ejb.multithreading;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.tsdes.misc.testutils.EmbeddedJeeSupport;

public abstract class CounterTestBaseJEE extends CounterTestBase {

    private static EmbeddedJeeSupport container = new EmbeddedJeeSupport();

    @BeforeEach
    public void initContainer()  {
        container.initContainer();
    }

    @AfterEach
    public void closeContainer() throws Exception {
        container.closeContainer();
    }

    @Override
    public Counter getCounter() {
        return  container.getEJB(getSingletonClass(), Counter.class);
    }

    protected abstract Class<? extends Counter> getSingletonClass();
}
