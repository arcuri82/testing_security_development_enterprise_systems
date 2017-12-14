package org.tsdes.intro.jee.ejb.stateful;

import javax.ejb.Stateless;

/*
    This is wrong: trying to have state in a @Stateless EJB
 */
@Stateless
public class StatelessCounter extends Counter{
}
