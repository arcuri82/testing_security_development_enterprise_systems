package org.tsdes.spring.kotlin;

/**
 * Created by arcuri82 on 17-Aug-17.
 */
public class JavaBase {

    public boolean startWithFoo(String s) {
        if(s == null){
            return false;
        }

        String foo = "foo";

        return s.startsWith(foo);
    }
}
