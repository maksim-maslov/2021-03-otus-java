package homework;

import homework.annotations.usage.AnnotationsUse;

public class Main {
    public static void main(String[] args) {
        Class<AnnotationsUse> clazz = AnnotationsUse.class;
        TestRunner.run(clazz);
    }
}
