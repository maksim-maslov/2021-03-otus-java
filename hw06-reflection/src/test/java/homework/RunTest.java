package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;
import homework.annotations.usage.AnnotationsUse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class RunTest {

    public static void runTests(Class<?> clazz) {

        int allRunTests = 0;
        int failedTests = 0;
        int passedTests = 0;

        try {

            System.out.println("Creating new object...");
            Constructor<?> constructor = clazz.getConstructor();
            Object object = constructor.newInstance();
            System.out.println();

            System.out.println("Execute annotated methods...");
            Method[] annotatedMethod = clazz.getDeclaredMethods();

            Method beforeMethod = null;
            Method afterMethod = null;
            ArrayList<Method> testMethods = new ArrayList<>();

            for (Method el : annotatedMethod) {
                if (el.isAnnotationPresent(Before.class)) {
                    beforeMethod = el;
                } else if (el.isAnnotationPresent(After.class)) {
                    afterMethod = el;
                } else if (el.isAnnotationPresent(Test.class)) {
                    testMethods.add(el);
                }
            }


            for (Method el : testMethods) {

                try {

                    beforeMethod.invoke(object);

                } catch (InvocationTargetException wrappedException) {

                    Throwable e = wrappedException.getCause();
                    System.out.println(el + " failed: " + e);

                    e.printStackTrace();
                }


                try {

                    allRunTests++;

                    el.invoke(object);

                    passedTests++;

                } catch (InvocationTargetException wrappedException) {

                    failedTests++;

                    Throwable e = wrappedException.getCause();
                    System.out.println(el + " failed: " + e);

                    e.printStackTrace();
                }


                try {

                    afterMethod.invoke(object);

                } catch (InvocationTargetException wrappedException) {

                    Throwable e = wrappedException.getCause();
                    System.out.println(el + " failed: " + e);

                    e.printStackTrace();
                }
            }

            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();

        System.out.println("--- all tests:" + allRunTests);
        System.out.println("--- failed tests:" + failedTests);
        System.out.println("--- passed tests:" + passedTests);
    }
}
