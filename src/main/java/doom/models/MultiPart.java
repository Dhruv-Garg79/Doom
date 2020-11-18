package doom.models;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiPart {
    private final Map<String, Object> map;

    public MultiPart() {
        map = new HashMap<>();
    }

    public String getText(String key) {
        if (!map.containsKey(key) || !(map.get(key) instanceof String))
            return null;

        return (String) map.get(key);
    }

    public File getFile(String key) {
        if (!map.containsKey(key) || !(map.get(key) instanceof File))
            return null;

        return (File) map.get(key);
    }

    public Set<Map.Entry<String, Object>> entrySet(){
        return map.entrySet();
    }

    public void put(String key, String value){
        map.put(key, value);
    }

    public void put(String key, File value){
        map.put(key, value);
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
