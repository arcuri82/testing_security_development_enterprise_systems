package org.tsdes.jee.ejb.framework.injection.examples;

import org.tsdes.jee.ejb.framework.injection.InjectorBaseSuite;
import org.tsdes.jee.ejb.framework.injection.Injector;


public class InjectorImp03TestNot extends InjectorBaseSuite {

    @Override
    protected Injector getInjector() {
        return new InjectorImp03();
    }
}