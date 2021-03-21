package com.irappelt.mymusic.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author iRappelt
 * @project: mymusic
 * @description:
 * @date: 2021/03/10 19:55
 * @version: v1.0
 */
public class Test {

    private String arg;
    private static String arg2;

//    public String fun() {
//        InnerTest innerTest = new InnerTest();
//    }

    class InnerTest {
        private String innerArg;

        private String staticInnerArg;

        public String innerfun() {
            return arg+innerArg+arg2;
        }
    }

    static class StaticTest {
        private String staticArg;

        public String staticFun() {
            return staticArg;
        }

    }

    public static void main(String[] args) {
//        InnerTest innerTest = new InnerTest();
//        Test test = new Test();
//        InnerTest innerTest = test.new InnerTest();
//
//        StaticTest staticTest = new StaticTest();
//        staticTest.staticFun();
//        Test.StaticTest staticTest1 = new Test.StaticTest();
//        staticTest1.staticFun();
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println(atomicInteger.getAndAdd(1));
        System.out.println(atomicInteger.getAndAdd(1));


    }




}

class Generic<T> extends Test.StaticTest {

    private T data;

    Generic(T data) {
        this.data = data;
    }
}
