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

        Map<String, List<Method>> annotatedMethods = getAnnotatedMethods(clazz);

        Statistics statistics = new Statistics();

        System.out.println("Execute test methods...");
        System.out.println();

        Method method = null;

        for (Method el : annotatedMethods.get("Test")) {

            try {

                Constructor<?> constructor = clazz.getConstructor();
                Object object = constructor.newInstance();


                try {

                    for (Method beforeMethod : annotatedMethods.get("Before")) {
                        method = beforeMethod;
                        method.invoke(object);
                    }

                    method = el;

                    statistics.setAllTests(statistics.getAllTests() + 1);

                    method.invoke(object);

                    statistics.setPassedTests(statistics.getPassedTests() + 1);

                    for (Method afterMethod : annotatedMethods.get("After")) {
                        method = afterMethod;
                        method.invoke(object);
                    }

                } catch (InvocationTargetException | IllegalAccessException wrappedException) {

                    if (method.isAnnotationPresent(Test.class)) {
                        statistics.setFailedTests(statistics.getFailedTests() + 1);
                    }

                    Throwable e = wrappedException.getCause();
                    System.out.println(method + " failed: " + e);

                }

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


    private static void printRunStatistics(Statistics statistics) {

        System.out.println();

        System.out.println("--- all tests: " + statistics.getAllTests());
        System.out.println("--- failed tests: " + statistics.getFailedTests());
        System.out.println("--- passed tests: " + statistics.getPassedTests());
    }


}
