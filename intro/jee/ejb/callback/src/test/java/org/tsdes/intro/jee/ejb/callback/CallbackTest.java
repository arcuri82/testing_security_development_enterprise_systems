package org.tsdes.intro.jee.ejb.callback;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tsdes.misc.testutils.EmbeddedJeeSupport;

import static org.junit.jupiter.api.Assertions.*;


public class CallbackTest {

    private static EmbeddedJeeSupport container = new EmbeddedJeeSupport();

    @BeforeEach
    public void initContainer()  {
        container.initContainer();
    }

    @AfterEach
    public void closeContainer() throws Exception {
        container.closeContainer();
    }


    @Test
    public void testPostConstruct(){

        A directInstance = new A();
        assertNull(directInstance.getValue());

        A a = container.getEJB(A.class);
        String res = a.getValue();

        assertNotNull(res);

        B b = container.getEJB(B.class);
        assertEquals(b.getValue(), res);
    }
}