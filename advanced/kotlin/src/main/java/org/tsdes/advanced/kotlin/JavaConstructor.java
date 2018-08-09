package org.tsdes.advanced.kotlin;

/**
 * Created by arcuri82 on 17-Aug-17.
 */
public class JavaConstructor {

    private final String s;
    private int x;

    public JavaConstructor(String s, int x) {
        this.s = s;
        this.x = x;
    }

    public String foo(){
        return s + x;
    }

    public String getS() {
        return s;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
