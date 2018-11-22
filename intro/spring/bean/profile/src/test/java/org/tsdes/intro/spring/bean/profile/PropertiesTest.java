package org.tsdes.intro.spring.bean.profile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.bean.jpa.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class PropertiesTest {

    @Value("${my.custom.foo}")
    private String foo;


    @Value("${my.custom.test}")
    private String test;

    //these would fail Spring initialization at runtime, because missing
//    @Value("${my.custom.main}")
//    private String main;
//    @Value("${my.custom.docker}")
//    private String docker;

    @Autowired
    private Environment env;


    @Test
    public void testPropertiesWithDefaultProfile(){

        assertEquals("test", foo);
        assertNotNull(test);

        assertEquals(foo, env.getProperty("my.custom.foo"));
        assertEquals(test, env.getProperty("my.custom.test"));

        assertNull(env.getProperty("my.custom.main"));
        assertNull(env.getProperty("my.custom.docker"));
    }
}
