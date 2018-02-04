package org.tsdes.intro.spring.testing.coverage.instrumentation;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Add instrumentations to keep track of which statements/lines
 * are covered during test execution
 */
public class CoverageClassVisitor extends ClassVisitor {

    private final String bytecodeClassName;

    public CoverageClassVisitor(ClassVisitor cv, ClassName className) {
        super(Opcodes.ASM5, cv);
        bytecodeClassName = className.getBytecodeName();
    }

    @Override
    public MethodVisitor visitMethod(int methodAccess,
                                     String name,
                                     String descriptor,
                                     String signature,
                                     String[] exceptions) {

        MethodVisitor mv = super.visitMethod(
                methodAccess, name, descriptor, signature, exceptions);

        /*
            Methods added by the compiler (eg synthetics and bridges) are
            not interesting, so we can just skip them. More info:

            https://docs.oracle.com/javase/tutorial/java/generics/bridgeMethods.html
            http://www.javaworld.com/article/2073578/java-s-synthetic-methods.html
         */
        if (isMethodSyntheticOrBridge(methodAccess)) {
            return mv;
        }

        mv = new LineCovMethodVisitor(mv, bytecodeClassName, name, descriptor);

        return mv;
    }

    private static boolean isMethodSyntheticOrBridge(int methodAccess){
        return (methodAccess & Opcodes.ACC_SYNTHETIC) > 0
                || (methodAccess & Opcodes.ACC_BRIDGE) > 0 ;
    }
}
