package org.tsdes.intro.spring.bean.configuration;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
//Note the lack of annotations here... this is just a basic Java class
public class NotAService {

    private final String value;


    public NotAService(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
