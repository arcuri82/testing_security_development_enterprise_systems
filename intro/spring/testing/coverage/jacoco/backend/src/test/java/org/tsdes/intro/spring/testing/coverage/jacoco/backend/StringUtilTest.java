package org.tsdes.intro.spring.testing.coverage.jacoco.backend;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilTest {

    @Test
    public void test(){

        int n = 10;
        String foo = StringUtil.getRandomString(n);
        assertNotNull(foo);
        assertTrue(foo.length() > 0);
        assertTrue(foo.length() <= n, "" + foo.length() +" > " + n);
    }
}