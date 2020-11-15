package doom.services;

import doom.models.Directory;

public interface DirectoryResolver {

    Directory resolveDirectory(Class<?> startupClass);

}
