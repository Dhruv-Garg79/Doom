package doom.utils;

import doom.sample.ExampleResource;
import org.junit.jupiter.api.Test;

import java.util.List;

class UtilsTest {

    @Test
    void getClassesInPackage() {
        List<Class<?>> classes = Utils.getAllClasses(ExampleResource.class);
        System.out.println("////////////////////////");
        for (Class<?> mClass : classes)
            System.out.println(mClass.getName());
    }
}