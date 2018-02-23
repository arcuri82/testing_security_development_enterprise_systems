package org.tsdes.intro.spring.deployment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class RedirectForwardHandler {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String forward(){
        return "forward:index.xhtml";
    }
}
