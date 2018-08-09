package org.tsdes.advanced.kotlin;

/**
 * Created by arcuri82 on 17-Aug-17.
 */
public class JavaElvis {

    class Link{
        Link next;
        String s;
    }

    public boolean fiveNextIsFoo(Link link){
        if(link == null ||
                link.next == null ||
                link.next.next == null ||
                link.next.next.next == null ||
                link.next.next.next.next == null ||
                link.next.next.next.next.next == null ||
                link.next.next.next.next.next.s == null){
            return false;
        }

        //all checks above are necessary to guarantee this
        //instruction does not throw a NPE
        return  link.next.next.next.next.next.s.equals("foo");
    }

    public boolean fiveNextIsFooWithCatch(Link link){

        try {
            return link.next.next.next.next.next.s.equals("foo");
        }catch (NullPointerException e){
            //this is more expensive, as exceptions need
            //to fill info from stacktrace
            return false;
        }
    }
}
