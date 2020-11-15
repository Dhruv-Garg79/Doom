package doom.services;

import java.util.List;

public interface ClassLocator {
    List<Class<?>> locateClasses(String dirPath) throws ClassCastException;
}
