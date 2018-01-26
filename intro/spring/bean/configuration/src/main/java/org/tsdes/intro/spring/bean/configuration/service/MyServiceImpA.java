package org.tsdes.intro.spring.bean.configuration.service;

import org.springframework.stereotype.Service;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@Service
public class MyServiceImpA implements MyService {
    @Override
    public String getValue() {
        return "A";
    }
}
