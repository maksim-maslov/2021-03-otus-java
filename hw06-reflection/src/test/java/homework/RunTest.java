package homework;

import homework.annotations.usage.AnnotationsUse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RunTest {
    public static void main(String[] args) {

        Class<AnnotationsUse> clazz = AnnotationsUse.class;
        runTests(clazz);
    }

    public static void runTests(Class clazz) {

        String[] annotations = {"homework.annotations.Before", "homework.annotations.Test", "homework.annotations.After"};

        int allRunTests = 0;
        int failedTests = 0;
        int passedTests = 0;

        try {

            System.out.println("Creating new object...");
            Constructor<AnnotationsUse> constructor = clazz.getConstructor();
            AnnotationsUse object = constructor.newInstance();
            System.out.println();

            System.out.println("Execute annotated methods...");
            Method[] annotatedMethod = clazz.getDeclaredMethods();

            for (String annotation : annotations) {

                System.out.println(annotation);

                for (Method el : annotatedMethod) {

                    if (el.isAnnotationPresent((Class<? extends Annotation>) Class.forName(annotation))) {

                        allRunTests++;

                        try {

                            el.invoke(object);
                            passedTests++;

                        } catch (InvocationTargetException wrappedException) {

                            Throwable e = wrappedException.getCause();
                            System.out.println(el + " failed: " + e);

                            failedTests++;

                            e.printStackTrace();
                        }
                    }
                }

                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();

        System.out.println("--- all tests:" + allRunTests);
        System.out.println("--- failed tests:" + failedTests);
        System.out.println("--- passed tests:" + passedTests);
    }
}
