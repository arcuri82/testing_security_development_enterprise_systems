package org.tsdes.jee.security.shirodb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 11-Dec-17.
 */
@RunWith(Arquillian.class)
public class UserEjbTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes")
                .addPackages(true, "org.apache.shiro")
                .addAsResource("createDB.sql")
                .addAsResource("META-INF/persistence.xml");
    }

    @Test
    public void testCreateUser(){

    }
}