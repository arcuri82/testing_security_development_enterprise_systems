package org.pg5100.jsf.jacoco.backend;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class DataEjbTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Data.class, DataEjb.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private DataEjb ejb;

    @Test
    public void testGetEmptyData(){
        assertNull(ejb.getData(1L));
    }

    @Test
    public void testGet(){
        String text = "foo";
        ejb.saveData(2L, text);

        assertEquals(text, ejb.getData(2L));
    }

    @Test
    public void testUpdateAndGet(){

        ejb.saveData(3L, "bar");
        String text = "foo";
        ejb.saveData(3L, text);

        assertEquals(text, ejb.getData(3L));
    }

}