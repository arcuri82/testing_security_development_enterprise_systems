package org.tsdes.jee.ejb.framework.proxy;


import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {

    private final List<CallLog> callLogs;

    public ObjectFactory(){
        callLogs = new ArrayList<>();
    }

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
            I instance = klassConcrete.newInstance();

            CallLog callLog = new CallLog(instance);
            callLogs.add(callLog);

            Object proxy = Proxy.newProxyInstance(klassInterface.getClassLoader(), new Class[]{klassInterface}, callLog);

            return (I) proxy;

        } catch (Exception e) {
            System.out.println("Failed to instantiate "+klassConcrete.getName()+" : "+e.toString());
            return null;
        }
    }
}
