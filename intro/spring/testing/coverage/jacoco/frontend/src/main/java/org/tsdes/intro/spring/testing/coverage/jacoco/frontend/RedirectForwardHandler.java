package org.tsdes.intro.spring.testing.coverage.jacoco.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/*
    When accessing localhost:8080 there is the request
    for the root element "/", ie that URL is changed into
    "localhost:8080/" (note the "/" at the end).
    By default, if there is no mapping for "/", most servers
    just returns "/index.html".
    However, here we have no such file, as our home page is
    called "home.xhtml".

    So, in this bean, we manually handle the root "/", by
    doing a "forward" (NOT a 302 "Redirect") toward our home
    page (the address bar on client browser will still point
    to "localhost:8080/").

    Note: this handling of HTTP request is done via @Controller
    and @RequestMapping
    We will see more on those in the "advanced" course, as those
    are the building blocks for REST API in Spring.
 */

@Controller
public class RedirectForwardHandler {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String forward(){
        return "forward:home.xhtml";
    }
}
