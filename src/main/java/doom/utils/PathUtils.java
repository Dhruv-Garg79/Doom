package doom.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathUtils {
    public static Pattern pathToRegex(String str){
        StringBuilder pattern = new StringBuilder();

        int n = str.length();
        int i = 0;

        while (i < n){
            if (str.charAt(i) == '/'){
                pattern.append("\\/");
            }
            else if (str.charAt(i) == '{'){
                while (str.charAt(i) != '}')
                    i++;
                pattern.append("([^\\/]*)");
            }
            else{
                pattern.append(str.charAt(i));
            }
            i++;
        }

        return Pattern.compile(pattern.toString());
    }

    public static Map<String, String> extractPathParams(Matcher matcher, String path){
        int group = 1, groupCount = matcher.groupCount() + 1, i = 0, n = path.length();

        Map<String, String> map = new HashMap<>();
        StringBuilder key = new StringBuilder();

        System.out.println(path + ' ' + groupCount);

        while (i < n && group < groupCount){
            if (path.charAt(i) == '{'){
                i++;
                while (i < n && path.charAt(i) != '}') {
                    key.append(path.charAt(i));
                    i++;
                }

                map.put(key.toString(), matcher.group(group));
                group++;
                key.setLength(0);
            }
            i++;
        }

        return map;
    }
}
