package org.tsdes.intro.spring.bean.configuration;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by arcuri82 on 26-Jan-18.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class NotAServiceTest {

    @Autowired
    private NotAService notAServiceFoo;

    @Autowired
    private NotAService notAServiceBar;

    @Autowired
    @Qualifier("notAServiceBar")
    private NotAService bar;

    @Autowired
    private NotAService notAServiceA;

    @Test
    public void testValues() {
        assertEquals("foo", notAServiceFoo.getValue());
        assertEquals("bar", notAServiceBar.getValue());
        assertEquals("bar", bar.getValue());
        assertEquals("A", notAServiceA.getValue());
    }

    @Test
    public void testSingletons() {
        assertEquals(notAServiceBar, bar);
    }
}