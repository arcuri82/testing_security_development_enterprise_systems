package org.tsdes.intro.spring.bean.configuration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MyServiceTestNot {

    /*
        This is ambiguous, because there are two possible
        instances that could be injected for MyService, so
        we need to specify which one by name.

        Note: if you are using IntelliJ Ultimate Edition, this
        variable should be flagged with a warning
     */

    @Autowired
    private MyService ambiguous;


    @Test
    public void testFailSpringInitialization(){
        /*
            This test will fail.
            Note the use of ending "*Not" in this class name...
            This is done to avoid this class be run in Maven during the build.
         */
    }
}
