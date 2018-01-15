package org.tsdes.intro.jee.ejb.framework.proxy.se.se;


import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;


public class ObjectFactory {

    private final List<CallLog> callLogs;

    public ObjectFactory(){
        callLogs = new ArrayList<>();
    }

    /**
        Return the number of times methods in proxied
        classes are called
     */
    public int getTotalInvocationCount(){
        return  callLogs.stream()
                .mapToInt(CallLog::getInvocationCount)
                .sum();
    }

    /*
        Note: as we can only create a proxy for an interface, here we also need to pass
        the concrete class
     */
    public <I>  I createInstance(Class<I> klassInterface, Class<? extends I> klassConcrete) {

        try {
            //create a new instance of concrete class
            I instance = klassConcrete.newInstance();

            //create a handler to enhance functionalities in proxied class
            CallLog callLog = new CallLog(instance);
            // keep track of all these handlers
            callLogs.add(callLog);

            //create the proxy class
            Object proxy = Proxy.newProxyInstance(
                    klassInterface.getClassLoader(), // the class loader
                    new Class[]{klassInterface}, // the interface for which we create a proxy
                    callLog); // the handler that extend the functionalities of the proxied class

            return (I) proxy;

        } catch (Exception e) {
            System.out.println("Failed to instantiate "+klassConcrete.getName()+" : "+e.toString());
            return null;
        }
    }
}
