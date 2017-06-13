package org.tsdes.testing.instrumentation;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LineCovMethodVisitor extends MethodVisitor {

    private final String className;
    private final String methodName;


    public LineCovMethodVisitor(MethodVisitor mv,
                                String className,
                                String methodName,
                                String descriptor) {
        super(Opcodes.ASM5, mv);

        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);


        /*
            This method is going to be called any time the "visitor" parsing
            the class definition does reach a LINENUMBER statement

            After a line, we add our instrumentation to record
            that the line has been just passed/executed.

            Here we push 2 elements on the stack, which
            are used to uniquely identify the line.
            Then, we do a call to ExecutionTracer that
            will pop these 2 elements as input parameters.
         */

        this.visitLdcInsn(className);
        this.visitLdcInsn(line);

        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                ClassName.get(ExecutionTracer.class).getBytecodeName(),
                ExecutionTracer.EXECUTED_LINE_METHOD_NAME,
                ExecutionTracer.EXECUTED_LINE_DESCRIPTOR,
                ExecutionTracer.class.isInterface()); //false

        /*
            While scanning the class file, record all the lines.
            This will be needed to computer the overall coverage
         */
        ExecutionTracer.initTarget(className, line);
    }


    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        /*
            we pushed 2 values on the stack, so we need to tell ASM
            that this instrumented method should have a frame for at
            least 2 elements.

            Note: as here we are instrumenting lines, it can be assumed
            that the frame is empty, and so we do
            Math.max(maxElementsAddedOnStackFrame, maxStack)
            instead of
            maxElementsAddedOnStackFrame +  maxStack
         */
        int maxElementsAddedOnStackFrame = 2;
        super.visitMaxs(Math.max(maxElementsAddedOnStackFrame, maxStack), maxLocals);
    }
}
