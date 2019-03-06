package org.tsdes.intro.spring.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = NONE)
public class ApplicationTest {


   @Test
   public void test(){

       /*
            not really testing anything here. just want to see the logs
            of when Spring starts.
            In the tests, we can have different settings, like putting on DEBUG
            some classes we are debugging.
            This can be done in a logback-test.xml file, which has precedence over
            logback.xml
        */
   }
}