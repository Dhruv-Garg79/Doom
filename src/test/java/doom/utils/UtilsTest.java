package doom.utils;

import doom.sample.ExampleResource;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void getClassesInPackage() {
        for (Class mClass : Utils.getClassesInPackage(ExampleResource.class.getPackageName()))
            System.out.println(mClass.getName());
    }
}