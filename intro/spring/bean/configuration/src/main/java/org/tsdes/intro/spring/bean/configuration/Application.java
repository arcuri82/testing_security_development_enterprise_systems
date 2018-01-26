package org.tsdes.intro.spring.bean.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@SpringBootApplication
public class Application {

    /*
        Note: @SpringBootApplication brings in @Configuration as well.
        Annotations can have annotations as well
     */

    @Bean
    public NotAService notAServiceFoo(){
        return new NotAService("foo");
    }

    @Bean
    public NotAService notAServiceBar(){
        return new NotAService("bar");
    }
}
