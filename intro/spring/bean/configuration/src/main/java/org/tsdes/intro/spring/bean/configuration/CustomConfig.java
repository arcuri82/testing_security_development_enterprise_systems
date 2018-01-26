package org.tsdes.intro.spring.bean.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tsdes.intro.spring.bean.configuration.service.MyService;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@Configuration
public class CustomConfig {

    @Bean
    public NotAService notAServiceA(@Autowired MyService myServiceImpA){
        return new NotAService(myServiceImpA.getValue());
    }
}
