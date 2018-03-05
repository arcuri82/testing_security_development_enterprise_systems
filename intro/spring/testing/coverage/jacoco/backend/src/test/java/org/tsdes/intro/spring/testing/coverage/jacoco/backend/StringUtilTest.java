package org.tsdes.intro.spring.testing.coverage.jacoco.backend;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void test(){

        int n = 10;
        String foo = StringUtil.getRandomString(n);
        assertNotNull(foo);
        assertTrue(foo.length() > 0);
        assertTrue("" + foo.length() +" > " + n, foo.length() <= n);
    }
}