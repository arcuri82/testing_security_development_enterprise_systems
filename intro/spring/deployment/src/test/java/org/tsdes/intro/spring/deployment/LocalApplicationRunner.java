package org.tsdes.intro.spring.deployment;

import org.springframework.boot.SpringApplication;

/**
 * Created by arcuri82 on 14-Dec-17.
 */
public class LocalApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, "--spring.profiles.active=test");
    }
}