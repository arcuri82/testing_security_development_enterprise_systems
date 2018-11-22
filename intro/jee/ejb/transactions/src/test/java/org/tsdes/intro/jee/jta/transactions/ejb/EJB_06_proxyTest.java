package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class EJB_06_proxyTest extends TestBase{


    @Test
    public void testProxy(){

        EJB_06_proxy ejb = getEJB(EJB_06_proxy.class);

        String proxiedClass = ejb.getClass().getName();
        String actualClass = ejb.getClassNameFromEJBInstance();

        //the actual, original class
        assertEquals(EJB_06_proxy.class.getName(), actualClass);


        // This is exactly equivalent to above assertion.
        // Spelled it out just to make it clear.
        // However, should not write it like this, as name/package refactoring would
        // not change this string, but it would change EJB_06_proxy.class.getName()
        assertEquals("org.tsdes.intro.jee.jta.transactions.ejb.EJB_06_proxy", actualClass);

        //proxy is a different story...
        assertNotEquals(proxiedClass, actualClass);

        System.out.println("\n\nProxy class name: "+proxiedClass+"\n");

        // NOTE: we do not know what name will be used, and it might even change
        // in different EJB containers and JEE versions.
        // This assertion is just for explanation, not something you should write,
        // as it is quite brittle
        assertTrue(proxiedClass.contains("_Generated_"));


        //proxy is still a valid EJB_06_proxy, ie a subclass
        assertTrue(EJB_06_proxy.class.isAssignableFrom(ejb.getClass()));

        //however, it will have extra methods
        Method[] proxiedMethods = ejb.getClass().getDeclaredMethods();
        assertNotEquals(proxiedMethods.length, ejb.getMethodsFromEJBInstance().length);

        Set<String> methodNames = Arrays.asList(ejb.getMethodsFromEJBInstance()).stream()
                .map(Method::getName)
                .collect(Collectors.toSet());

        Set<String> addedMethods = Arrays.asList(proxiedMethods).stream()
                .map(Method::getName)
                .filter(name -> ! methodNames.contains(name))
                .collect(Collectors.toSet());


        for(String name : addedMethods){
            System.out.println("Added method: "+name);
        }

        System.out.println("\n\n");
    }
}
