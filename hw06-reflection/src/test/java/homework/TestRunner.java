package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestRunner {

    public static void run(Class<?> clazz) {

        Map<String, List<Method>> annotatedMethods = new HashMap<>();
        annotatedMethods = getAnnotatedMethods(clazz);


        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("allRunTests", 0);
        statistics.put("passedTests", 0);
        statistics.put("failedTests", 0);


        System.out.println("Execute test methods...");
        System.out.println();

        for (Method el : annotatedMethods.get("Test")) {

            try {

                Constructor<?> constructor = clazz.getConstructor();
                Object object = constructor.newInstance();

                runAnnotatedMethod(annotatedMethods.get("After").get(0), object, false, statistics);
                runAnnotatedMethod(el, object, true, statistics);
                runAnnotatedMethod(annotatedMethods.get("After").get(0), object, false, statistics);

            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException wrappedException) {

                Throwable e = wrappedException.getCause();
                System.out.println(el + " failed: " + e);
            }

            printRunStatistics(statistics);
        }
    }


    private static Map<String, List<Method>> getAnnotatedMethods(Class<?> clazz) {

        Method[] declaredMethods = clazz.getDeclaredMethods();

        Map<String, List<Method>> annotatedMethods = new HashMap<>();
        annotatedMethods.put("Test", new ArrayList<>());
        annotatedMethods.put("Before", new ArrayList<>());
        annotatedMethods.put("After", new ArrayList<>());


        for (Method el : declaredMethods) {
            if (el.isAnnotationPresent(Test.class)) {
                annotatedMethods.get("Test").add(el);
            } else if (el.isAnnotationPresent(Before.class)) {
                annotatedMethods.get("Before").add(el);
            } else if (el.isAnnotationPresent(After.class)) {
                annotatedMethods.get("After").add(el);
            }
        }

        return annotatedMethods;
    }


    private static void runAnnotatedMethod(Method method, Object object, boolean isTestAnnotatedMethod, Map<String, Integer> statistics) {

        try {

            if (isTestAnnotatedMethod) {
                statistics.put("allRunTests", statistics.get("allRunTests") + 1);
            }

            method.invoke(object);

            if (isTestAnnotatedMethod) {
                statistics.put("passedTests", statistics.get("passedTests") + 1);
            }

        } catch (InvocationTargetException | IllegalAccessException wrappedException) {

            if (isTestAnnotatedMethod) {
                statistics.put("failedTests", statistics.get("failedTests") + 1);
            }

            Throwable e = wrappedException.getCause();
            System.out.println(method + " failed: " + e);

        }
    }


    private static void printRunStatistics(Map<String, Integer> statistics) {

        System.out.println();

        System.out.println("--- all tests: " + statistics.get("allRunTests"));
        System.out.println("--- failed tests: " + statistics.get("failedTests"));
        System.out.println("--- passed tests: " + statistics.get("passedTests"));
    }
}
