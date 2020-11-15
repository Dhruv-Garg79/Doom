package doom.services;

import doom.enums.DirectoryType;
import doom.models.Directory;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static doom.utils.Constants.JAR_FILE_EXTENSION;

public class DirectoryResolverImpl implements DirectoryResolver {

    @Override
    public Directory resolveDirectory(Class<?> startupClass) {
        String dir = getDirectory(startupClass);

        return new Directory(dir, getDirectoryType(dir));
    }

    private String getDirectory(Class<?> cls){
        return URLDecoder.decode(cls.getProtectionDomain().getCodeSource().getLocation().getFile(), StandardCharsets.UTF_8);
    }

    private DirectoryType getDirectoryType(String dir){
        File file = new File(dir);

        if (!file.isDirectory() && dir.endsWith(JAR_FILE_EXTENSION)){
            return DirectoryType.JAR_FILE;
        }

        return DirectoryType.Directory;
    }
}
