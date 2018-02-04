package org.tsdes.intro.spring.testing.coverage.jacoco.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataServiceTest {


    @Autowired
    private DataService ejb;

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