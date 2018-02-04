package org.tsdes.intro.spring.testing.coverage.jacoco.backend;

/**
 * Just an example class which is not called in this module,
 * and no test for it.
 * But, it is called in the "frontend" module.
 * This is needed to see how "transitive" coverage works.
 */
public class DefaultDataValue {


    public static String get(){
        return "No data";
    }
}
