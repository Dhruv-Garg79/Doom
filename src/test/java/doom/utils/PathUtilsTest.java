package doom.utils;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PathUtilsTest {

    @Test
    void pathToRegex() {
        Pattern pattern = PathUtils.pathToRegex("/api/resource/example");
        assertTrue(pattern.matcher("/api/resource/example").matches());

        pattern = PathUtils.pathToRegex("/api/resource/{name}");
        Matcher matcher = pattern.matcher("/api/resource/dhruv");
        assertTrue(matcher.matches());

        pattern = PathUtils.pathToRegex("/api/resource/{name}/{id}/");
        matcher = pattern.matcher("/api/resource/dhruv/123/");
        assertTrue(matcher.matches());
    }

    @Test
    void extractPathParams() throws URISyntaxException {
        String path = "/api/resource/dhruv/123/marks/879";
        String routePath = "/api/resource/{name}/{id}/marks/{mark}";

        Pattern pattern = PathUtils.pathToRegex(routePath);
        Matcher matcher = pattern.matcher(path);

        assertTrue(matcher.matches());

        System.out.println(pattern);
        System.out.println("groupCount : " + matcher.groupCount());

        System.out.println(PathUtils.extractPathParams(matcher, routePath));
    }
}