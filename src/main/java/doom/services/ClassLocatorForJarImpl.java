package doom.services;

import doom.exceptions.ClassLocatorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static doom.utils.Constants.CLASS_FILE_EXTENSION;

public class ClassLocatorForJarImpl implements ClassLocator {
    @Override
    public List<Class<?>> locateClasses(String dirPath) throws ClassCastException {
        final List<Class<?>> classes = new ArrayList<>();
        int m = CLASS_FILE_EXTENSION.length();

        try {
            JarFile jarFile = new JarFile(dirPath);
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()){
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();

                if (!name.endsWith(CLASS_FILE_EXTENSION)){
                    continue;
                }

                int n = name.length();

                String className = name.substring(0, n - m).replaceAll("/", ".");
                classes.add(Class.forName(className));
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Class not found");
            throw new ClassLocatorException(e.getMessage(), e);
        }

        return classes;
    }
}
