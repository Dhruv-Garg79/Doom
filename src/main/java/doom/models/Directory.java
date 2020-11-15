package doom.models;

import doom.enums.DirectoryType;

public class Directory {
    public final String path;
    public final DirectoryType directoryType;

    public Directory(String path, DirectoryType directoryType) {
        this.path = path;
        this.directoryType = directoryType;
    }

    public String getPath() {
        return path;
    }

    public DirectoryType getDirectoryType() {
        return directoryType;
    }

    @Override
    public String toString() {
        return "Directory{" +
                "path='" + path + '\'' +
                ", directoryType=" + directoryType +
                '}';
    }
}
