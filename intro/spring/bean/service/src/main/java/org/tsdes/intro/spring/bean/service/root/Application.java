package org.tsdes.intro.spring.bean.service.root;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the SpringBoot application.
 * Only one class can be annotated with @SpringBootApplication
 * If you have more, Spring runtime will complain and throw exception.
 *
 * By default, Spring will automatically scan all files in current
 * package, and recursively in the subpackage.
 * It will create "beans" based on what found on classpath.
 *
 * Different kinds of beans, but essentially is the same concept of
 * proxy classes like EJB.
 *
 * Created by arcuri82 on 25-Jan-18.
 */
@SpringBootApplication
public class Application {
}
