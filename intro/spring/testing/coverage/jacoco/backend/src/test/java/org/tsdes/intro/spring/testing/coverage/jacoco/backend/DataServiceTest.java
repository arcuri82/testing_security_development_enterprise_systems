package org.tsdes.intro.spring.testing.coverage.jacoco.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(SpringExtension.class)
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