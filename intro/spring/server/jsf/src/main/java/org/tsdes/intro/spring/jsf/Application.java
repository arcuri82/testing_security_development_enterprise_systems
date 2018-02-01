package org.tsdes.intro.spring.jsf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by arcuri82 on 12-Dec-17.
 */
@SpringBootApplication
public class Application {

    /**
     * In a "packaged" Java application, this method would be the entry point.
     * This is also useful to have for debugging, as in
     * an IDE you can just right-click on this class to start
     * the whole application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
