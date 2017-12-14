package org.tsdes.intro.jee.jsf.examples.ex04;


import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class RequestCounter extends Counter {
}
