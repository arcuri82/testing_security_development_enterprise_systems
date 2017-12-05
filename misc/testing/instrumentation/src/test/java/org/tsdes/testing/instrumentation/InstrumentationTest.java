package org.tsdes.testing.instrumentation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 17-Mar-17.
 */
public class InstrumentationTest {

    @Before
    public void reset(){
        ExecutionTracer.reset();
    }

    @Test
    public void testNotInstrumented_new(){

        /*
            Instrumentation is done only when using an instrumenting classloader
         */

        double cov = ExecutionTracer.getCoverage(FooImp.class.getName());

        assertEquals(0, cov, 0.0001);

        Foo foo = new FooImp();
        foo.printString();

        cov = ExecutionTracer.getCoverage(FooImp.class.getName());

        assertEquals(0, cov, 0.0001);
    }

    @Test
    public void testNotInstrumented_classLoader() throws Exception {

        double cov = ExecutionTracer.getCoverage(FooImp.class.getName());

        assertEquals(0, cov, 0.0001);

        /*
            When we see a class for the first time in a method, such class will be loaded by the same
            classloader as the one of the method itself.
            Therefore, the following command is exactly equivalent to:

            Foo foo = new FooImp();

            More interestingly, the following "loadClass(...)" is actually taking a cached, already
            loaded class definition. Why? Because FooImp.class was already loaded at the
            beginning of the test when executed:

            double cov = ExecutionTracer.getCoverage(FooImp.class.getName());
         */
        Foo foo = (Foo) this.getClass().getClassLoader().loadClass(FooImp.class.getName()).newInstance();
        foo.printString();

        cov = ExecutionTracer.getCoverage(FooImp.class.getName());

        assertEquals(0, cov, 0.0001);
    }

    @Test
    public void testClassloaderMismatch() throws Exception {

        InstrumentingClassLoader cl = new InstrumentingClassLoader(FooImp.class.getName());

        Foo notInst = new FooImp();
        assertTrue(notInst instanceof FooImp);


        //load the class with our new classloader
        Foo instr = (Foo) cl.loadClass(FooImp.class.getName()).newInstance();
        assertNotNull(instr);

        /*
            Now, we have 2 class definition with same, matching name.
            But a class is NOT uniquely identified by just its full name, eg

            org.tsdes.testing.instrumentation.FooImp

            besides the class name, the classloader that loaded it does identify it.
            This means that now we have 2 DIFFERENT classes with same name and same
            code (well, one its extended/instrumented). But both do implement the
            same interface, ie Foo
         */
        assertEquals(notInst.getClass().getName(), instr.getClass().getName());
        assertFalse(instr instanceof FooImp);
        assertNotEquals(notInst.getClass(), instr.getClass());
    }

    @Test
    public void testCoverage() throws Exception{

        String name = FooImp.class.getName();
        InstrumentingClassLoader cl = new InstrumentingClassLoader(name);

        double a = ExecutionTracer.getCoverage(name);
        assertEquals(0, a, 0.0001);

        Foo foo = (Foo) cl.loadClass(FooImp.class.getName()).newInstance();

        foo.printString();
        double b = ExecutionTracer.getCoverage(name);
        assertTrue(b > a);

        foo.add(1,2);
        double c = ExecutionTracer.getCoverage(name);
        assertTrue(c > b);

        foo.add(1,2);
        double d = ExecutionTracer.getCoverage(name);
        //calling same method/parameters, coverage should stay the same
        assertTrue(d == c);

        foo.isPositive(5);
        double e = ExecutionTracer.getCoverage(name);
        assertTrue(e > d);

        foo.isPositive(-3);
        double f = ExecutionTracer.getCoverage(name);
        assertTrue(f > e);

        assertEquals(1.0, f, 0.0001);
    }

}