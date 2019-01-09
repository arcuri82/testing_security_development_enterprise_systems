package org.tsdes.intro.jee.ejb.multithreading.jse;

import org.tsdes.intro.jee.ejb.multithreading.Counter;
import org.tsdes.intro.jee.ejb.multithreading.CounterTestBase;

/**
 * Created by arcuri82 on 09-Jan-19.
 */
public class Example07Test  extends CounterTestBase {

    @Override
    protected Counter getCounter() {
        return new Example07();
    }
}