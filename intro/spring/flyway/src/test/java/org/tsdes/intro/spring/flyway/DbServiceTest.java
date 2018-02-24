package org.tsdes.intro.spring.flyway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = NONE)
public class DbServiceTest {

    @Autowired
    private DbService service;

    @Test
    public void testDatabase(){

        List<First> firsts = service.getAllFirst();
        List<Second> seconds = service.getAllSecond();

        assertEquals(2, firsts.size());
        assertEquals(1, seconds.size());
    }
}