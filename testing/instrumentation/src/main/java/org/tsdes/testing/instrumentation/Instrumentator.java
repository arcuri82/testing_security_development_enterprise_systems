package org.tsdes.testing.instrumentation;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Instrumentator {

    private final List<String> prefixes;

    public Instrumentator(String packagePrefixesToCover) {
        Objects.requireNonNull(packagePrefixesToCover);

        prefixes = Arrays.stream(
                packagePrefixesToCover.split(","))
                .map(s -> s.trim())
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (prefixes.isEmpty()) {
            throw new IllegalArgumentException("You have to specify at least one non-empty prefix, e.g. 'com.yourapplication'");
        }


    }

    /**
     * Get the raw bytes of instrumented class with name {@code className}
     *
     * @param classLoader
     * @param className
     * @param reader
     * @return
     */
    public byte[] transformBytes(ClassLoader classLoader, ClassName className, ClassReader reader) {
        Objects.requireNonNull(classLoader);
        Objects.requireNonNull(className);
        Objects.requireNonNull(reader);

        if (!canInstrumentForCoverage(className)) {
            throw new IllegalArgumentException("Cannot instrument " + className);
        }


        int asmFlags = ClassWriter.COMPUTE_FRAMES;
        ClassWriter writer = new ComputeClassWriter(asmFlags);
        ClassVisitor cv = writer;

        //this is very we hook our instrumentation
        cv = new CoverageClassVisitor(cv, className);

        //avoid reading frames, as we re-compute them
        int readFlags = ClassReader.SKIP_FRAMES;

        /*
            This is using the "Visitor Pattern".
            In a nutshell, we scan the whole class definition, and at each
            element we "print" in the "writer" buffer.
            On such buffer we also add our instrumentation.
            Then, once we are done traversing the "reader", we convert
            the buffer in the "writer" into byte[] data
         */
        reader.accept(cv, readFlags);

        return writer.toByteArray();
    }


    public boolean canInstrumentForCoverage(ClassName className) {

        return prefixes.stream()
                .anyMatch(s -> className.getFullNameWithDots().startsWith(s));
    }
}