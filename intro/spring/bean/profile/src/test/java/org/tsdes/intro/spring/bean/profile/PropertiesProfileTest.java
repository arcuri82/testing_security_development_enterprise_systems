package org.tsdes.intro.spring.bean.profile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.bean.jpa.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Created by arcuri82 on 26-Jan-18.
 */
@ActiveProfiles("bar") //activate profile, load configs from application-bar.properties
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class PropertiesProfileTest {

    @Value("${my.custom.foo}")
    private String foo;

    @Value("${my.custom.test}")
    private String test;

    @Value("${my.custom.bar}")
    private String bar;

    @Autowired
    private Environment env;

    @Test
    public void testPropertiesWithDockerProfile(){

        assertNotNull(foo);
        assertNotNull(test);
        assertNotNull(bar);

        assertEquals(foo, env.getProperty("my.custom.foo"));
        assertEquals(test, env.getProperty("my.custom.test"));
        assertEquals(bar, env.getProperty("my.custom.bar"));

        assertEquals("bar", foo);
        assertEquals(foo, bar);
        assertNotEquals(foo, test);
    }
}
