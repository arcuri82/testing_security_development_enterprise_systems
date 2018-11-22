package org.tsdes.intro.spring.deployment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = NONE)
public class CounterServiceTest {

    @Autowired
    private CounterService service;

    @Test
    public void testCounter() {

        String name = "testCounter()";

        long n = service.getValueForCounter(name);
        assertEquals(0, n);

        service.increment(name);

        n = service.getValueForCounter(name);
        assertEquals(1, n);
    }
}