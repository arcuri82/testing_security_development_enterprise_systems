package org.tsdes.testing.instrumentation;

/**
 * Created by arcuri82 on 17-Mar-17.
 */
public class FooImp implements Foo{

    @Override
    public void printString(){
        System.out.println("printFoo");
    }

    @Override
    public int add(int x, int y){
        int res = x + y;
        System.out.println("Result is: "+ res);
        return res;
    }

    @Override
    public  boolean isPositive(double x){
        if(x > 0){
            return true;
        } else {
            return false;
        }
    }

    /*
        To look at the bytecode, in IntelliJ you need to install the
        "ASM Bytecode Outline" plugin. Then right click and select
        "Show Bytecode outline":

public class org/tsdes/testing/instrumentation/FooImp implements org/tsdes/testing/instrumentation/Foo  {

  // compiled from: FooImp.java

  // access flags 0x1
  public <init>()V
   L0
    LINENUMBER 6 L0
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
   L1
    LOCALVARIABLE this Lorg/tsdes/testing/instrumentation/FooImp; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x1
  public printString()V
   L0
    LINENUMBER 10 L0
    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    LDC "printFoo"
    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
   L1
    LINENUMBER 11 L1
    RETURN
   L2
    LOCALVARIABLE this Lorg/tsdes/testing/instrumentation/FooImp; L0 L2 0
    MAXSTACK = 2
    MAXLOCALS = 1

  // access flags 0x1
  public add(II)I
   L0
    LINENUMBER 15 L0
    ILOAD 1
    ILOAD 2
    IADD
    ISTORE 3
   L1
    LINENUMBER 16 L1
    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    NEW java/lang/StringBuilder
    DUP
    INVOKESPECIAL java/lang/StringBuilder.<init> ()V
    LDC "Result is: "
    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
    ILOAD 3
    INVOKEVIRTUAL java/lang/StringBuilder.append (I)Ljava/lang/StringBuilder;
    INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;
    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
   L2
    LINENUMBER 17 L2
    ILOAD 3
    IRETURN
   L3
    LOCALVARIABLE this Lorg/tsdes/testing/instrumentation/FooImp; L0 L3 0
    LOCALVARIABLE x I L0 L3 1
    LOCALVARIABLE y I L0 L3 2
    LOCALVARIABLE res I L1 L3 3
    MAXSTACK = 3
    MAXLOCALS = 4

  // access flags 0x1
  public isPositive(D)Z
   L0
    LINENUMBER 22 L0
    DLOAD 1
    DCONST_0
    DCMPL
    IFLE L1
   L2
    LINENUMBER 23 L2
    ICONST_1
    IRETURN
   L1
    LINENUMBER 25 L1
   FRAME SAME
    ICONST_0
    IRETURN
   L3
    LOCALVARIABLE this Lorg/tsdes/testing/instrumentation/FooImp; L0 L3 0
    LOCALVARIABLE x D L0 L3 1
    MAXSTACK = 4
    MAXLOCALS = 3
}

     */
}
