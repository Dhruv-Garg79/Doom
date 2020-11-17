package doom.models;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultiPart {
    private final Map<String, Object> map;

    public MultiPart() {
        map = new HashMap<>();
    }

    public String getText(String key) {
        if (!map.containsKey(key))
            return null;

        return (String) map.get(key);
    }

    public File getFile(String key) {
        if (!map.containsKey(key))
            return null;

        return (File) map.get(key);
    }

    public void put(FileItem item) throws IOException {
        if (item.getHeaders().getHeader("Content-Type") == null) {
            map.put(item.getFieldName(), new String(item.get()));
        }
        else {
            System.out.println(item.getName());
            String[] name = item.getName().split("\\.");
            File file = File.createTempFile(name[0], "." + (name.length < 2 ? "txt" : name[1]));

            try(FileOutputStream outputStream = new FileOutputStream(file)){
                outputStream.write(item.get());
            }

            file.deleteOnExit();
            map.put(item.getFieldName(), file);
        }
    }
}
