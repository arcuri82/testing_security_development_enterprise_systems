package org.tsdes.intro.spring.bean.async;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // IMPORTANT: otherwise @Async will not work
@SpringBootApplication
public class Application {
}
