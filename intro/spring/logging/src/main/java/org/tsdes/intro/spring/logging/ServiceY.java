package org.tsdes.intro.spring.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServiceY {

    private static final Logger log = LoggerFactory.getLogger(ServiceY.class);

    public void callService(int a, String s){

        /*
            String concatenation in DEBUG and INFO mode are usually bad.
            Point is, often those are disabled, and so all those extra
            string concatenations are just a a waste of CPU cycles...
         */
        log.debug("BAD: calling Y with '" + a + "' and '"+ s + "' as input");

        /*
            to avoid the issue with string concatenation, can use interpolation.
            SLF4J will treat any {} as a placeholder, which is then replaced by
            the following input parameters in the log method, in order.
            I.e., the first {} will be replaced by the content of "a", whereas the
            second {} will be replaced by the content of "s"
         */
        log.debug("GOOD: calling Y with '{}' and '{}' as input", a, s);


        throw new RuntimeException("Something weird happened");
    }
}
