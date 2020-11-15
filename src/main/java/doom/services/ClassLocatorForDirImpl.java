package doom.services;

import doom.exceptions.ClassLocatorException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static doom.utils.Constants.CLASS_FILE_EXTENSION;

public class ClassLocatorForDirImpl implements ClassLocator {
    private final List<Class<?>> classes;

    public ClassLocatorForDirImpl() {
        classes = new ArrayList<>();
    }

    @Override
    public List<Class<?>> locateClasses(String dirPath) throws ClassCastException {
        File dir = new File(dirPath);

        if (!dir.isDirectory()) throw new ClassLocatorException("Invalid directory " + dirPath);

        for (File file : Objects.requireNonNull(dir.listFiles())) scanDir(file, "");

        return classes;
    }

    private void scanDir(File dir, String packageName) {
        int m = CLASS_FILE_EXTENSION.length();
        packageName += dir.getName() + '.';

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanDir(file, packageName);
                continue;
            }

            String name = file.getName();
            if (!name.endsWith(CLASS_FILE_EXTENSION)) {
                continue;
            }

            try {

                int n = name.length();
                String className = packageName + name.substring(0, n - m).replaceAll("/", ".");
                classes.add(Class.forName(className));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
