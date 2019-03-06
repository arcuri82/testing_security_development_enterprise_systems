package org.tsdes.intro.spring.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ServiceX {

    /*
        Loggers will be named. It is a common practice to name a logger with the
        name of the class in which it is used. So each class will have its own
        logger. This has a few benefits:
        - can easily find out from which class a log statement is from, as the name of
          logger can be in it
        - can set a lower logging level (eg DEBUG) for a specific class we want to debug
        - can use package prefixes to set up logging level for all loggers in a package.
          For example, if you want to see what Spring is doing, could set to DEBUG the
          loggers for "org.springframework"
        The logging levels (and the appenders) can be specified in a logback.xml file.
     */
    private static final Logger log = LoggerFactory.getLogger(ServiceX.class);

    @Autowired
    private ServiceY y;

    @PostConstruct
    public void init(){

        log.debug("Initializing Service X");

        try{
            y.callService(42, "foo");
        }catch (Exception e){
            /*
                note: in case of "error", string concatenation is fine, as "hopefully"
                should happen seldom, and anyway we should ALWAYS log errors
             */
            log.error("Error when calling service Y: " + e.getMessage());
        }

        log.info("Service X is initialized");
    }
}
