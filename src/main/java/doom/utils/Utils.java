package doom.utils;

import doom.enums.DirectoryType;
import doom.models.Directory;
import doom.services.ClassLocator;
import doom.services.ClassLocatorForDirImpl;
import doom.services.ClassLocatorForJarImpl;
import doom.services.DirectoryResolverImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Utils {
    public static List<Class<?>> getAllClasses(Class<?> projectClass) {
        Directory directory = new DirectoryResolverImpl().resolveDirectory(projectClass);
        System.out.println(directory);

        ClassLocator locator;
        if (directory.directoryType == DirectoryType.JAR_FILE)
            locator = new ClassLocatorForJarImpl();
        else locator = new ClassLocatorForDirImpl();

        return locator.locateClasses(directory.path);
    }

    public static <T> Object getObjectForClass(Class<T> mClass) {
        Constructor<T> constructor = null;
        try {
            constructor = mClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
