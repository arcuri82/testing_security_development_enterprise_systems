package org.tsdes.intro.spring.testing.coverage.instrumentation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Methods of this class will be injected in the SUT to
 * keep track of what the tests do execute/cover.
 */
public class ExecutionTracer {

    /**
     * WARNING:
     * as a rule of thumb, you should NOT use static variables with modifiable state
     * (final primitive constants and immutable objects are OK).
     * What done here is a very, very special case. Think about it as the exception
     * that does confirm the rule.
     *
     *
     * Key -> class name
     * </ br>
     * Value -> Map from line number to boolean stating if covered
     */
    private static final Map<String, Map<Integer, Boolean>> coverage =
            new ConcurrentHashMap<>(65536);


    public static void reset() {
        coverage.values().stream().forEach(m ->
                    m.keySet().stream().forEach(k -> m.put(k, false))
                );
    }


    public static final String EXECUTED_LINE_METHOD_NAME = "executedLine";
    /**
     * We need to uniquely identify a method, which is done by its name and
     * its parameter types.
     * The type descriptors in the JVM have their own symbols:
     * L<class>; ->  object of given class
     * I -> int
     * V -> void
     * (XY)Z -> input parameters of type X and Y, where return type of method is Z
     *
     * so "(Ljava/lang/String;I)V" means String and int as input, void as output
     */
    public static final String EXECUTED_LINE_DESCRIPTOR = "(Ljava/lang/String;I)V";

    /**
     * Report on the fact that a given line has been executed.
     * This is method that we want to inject in the instrumented classes.
     */
    public static void executedLine(String className, int line) {

        coverage.get(className).put(line, true);
    }


    public static void initTarget(String className, int line){

        coverage.computeIfAbsent(className, k -> new ConcurrentHashMap<>())
                .put(line, false);
    }

    public static double getCoverage(String name){

        String bytecodeName = ClassName.get(name).getBytecodeName();

        if(! coverage.containsKey(bytecodeName)){
            System.out.println("Class "+name+" was not instrumented");
            return 0;
        }

        Map<Integer, Boolean> map = coverage.get(bytecodeName);
        return (double) map.values().stream().filter(v -> v).count() /  (double) map.size();
    }
}
