package org.tsdes.intro.jee.ejb.framework.proxy.se.se;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class CallLog implements InvocationHandler {

    //using an atomic integer just to avoid multithreading issues
    private final AtomicInteger counter = new AtomicInteger(0);

    //our original instance
    private final Object instance;

    public CallLog(Object instance) {
        this.instance = instance;
    }

    /*
        This method is going to be called every time a method on
        the proxy class is called
     */
    @Override
    public Object invoke(
            Object proxy, // a reference to the proxy class
            Method method, // the descriptor of the called method in the proxy
            Object[] args // the inputs of the called method  in the proxy
    ) throws Throwable {

        /*
            Code executed before call on original instance.

            At each invocation, increase counter and log on console.
            In other words, we just keep track of how often the
            methods are called.
         */
        counter.incrementAndGet();
        System.out.println("INTERCEPTED CALL TO: "+method.getName());

        /*
            note: the actual invocation is done on "instance" and not "proxy".
            This is done via reflection, by using the Method descriptor.

            So, something like:

            foo.someMethod(x)

            would become when using reflection:

            methodDescriptor("someMethod").invoke(foo, x)
         */
        return method.invoke(instance, args);
    }

    public int getInvocationCount(){
        return counter.get();
    }
}
