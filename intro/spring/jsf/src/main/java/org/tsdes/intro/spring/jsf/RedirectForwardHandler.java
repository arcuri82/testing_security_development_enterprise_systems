package org.tsdes.intro.spring.jsf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/*
    Special bean that we use to handle HTTP requests.
    In particular, we want to intercept the requests for the root "/",
    and return the content of index.html
 */
@Controller
public class RedirectForwardHandler {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String forward(){
        return "forward:index.html";
    }
}
