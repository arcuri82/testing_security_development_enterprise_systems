package org.tsdes.jee.ejb.framework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class CallLog implements InvocationHandler {

    //using an atomic integer just to avoid multitreading issues
    private final AtomicInteger counter = new AtomicInteger(0);

    private final Object instance;

    public CallLog(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //at each invocation, increase counter and log on console
        counter.incrementAndGet();
        System.out.println("INTERCEPTED CALL TO: "+method.getName());

        //note: the actual invocation is done on "instance" and not "proxy"
        return method.invoke(instance, args);
    }

    public int getInvocationCount(){
        return counter.get();
    }
}
