package org.tsdes.intro.spring.bean.profile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.spring.bean.jpa.Application;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@ActiveProfiles("docker") //activate profile, load configs from application-docker.yml
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PropertiesProfileTest {

    @Value("${my.custom.foo}")
    private String foo;

    @Value("${my.custom.test}")
    private String test;

    @Value("${my.custom.docker}")
    private String docker;

    @Autowired
    private Environment env;

    @Test
    public void testPropertiesWithDockerProfile(){

        assertNotNull(foo);
        assertNotNull(test);
        assertNotNull(docker);

        assertEquals(foo, env.getProperty("my.custom.foo"));
        assertEquals(test, env.getProperty("my.custom.test"));
        assertEquals(docker, env.getProperty("my.custom.docker"));

        assertEquals("docker", foo);
        assertEquals(foo, docker);
        assertNotEquals(foo, test);
    }
}
