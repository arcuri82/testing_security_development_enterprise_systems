package org.tsdes.jee.security.shirodb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 11-Dec-17.
 */
@RunWith(Arquillian.class)
public class UserEjbIT {

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        String name = "shiro-db.war";
        return ShrinkWrap.create(ZipImporter.class, name)
                .importFrom(new File("target/" + name))
                .as(WebArchive.class);
    }


    @EJB
    private UserEjb ejb;

    @Test
    public void testCreateUser(){

        String name = "foo";
        String password = "123";

        assertNull(ejb.findUser(name));

        ejb.createUser(name, password);

        UserEntity user = ejb.findUser(name);

        assertNotNull(user);
        assertEquals(name, user.getUsername());
    }
}