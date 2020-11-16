package doom.models;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import doom.enums.HttpMethods;
import doom.enums.MediaType;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private final HttpExchange exchange;
    private final Map<String, String> queryParams;
    private String path;
    private HttpMethods method;
    private Map<String, String> pathParams;
    private RequestBody<?> requestBody;

    public Request(HttpExchange exchange) {
        this.exchange = exchange;

        this.path = exchange.getRequestURI().getPath();
        method = HttpMethods.valueOf(exchange.getRequestMethod());

        queryParams = parseQueryParams(exchange.getRequestURI().getQuery(), false);

        if (exchange.getRequestHeaders().get("Content-Type") != null) parseBody();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethods getMethod() {
        return method;
    }

    public void setMethod(HttpMethods method) {
        this.method = method;
    }

    public HttpExchange getExchange() {
        return exchange;
    }

    public String getQueryParam(String key) {
        return queryParams.get(key);
    }

    public String getPathParam(String key) {
        return pathParams.get(key);
    }

    public RequestBody<?> getBody() {
        return requestBody;
    }

    private void parseBody() {
        Headers headers = exchange.getRequestHeaders();
        String bodyContentType = headers.get("Content-Type").get(0);
        System.out.println(this.hashCode() + "............................");

        try (InputStream bodyStream = exchange.getRequestBody()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = bodyStream.read(buffer)) > 0) {
                sb.append(new String(buffer));
            }

            if (bodyContentType.equals(MediaType.JSON.getVal())) {
                requestBody = new RequestBody<>(new JSONObject(sb.toString()));
            }
            else if (bodyContentType.equals(MediaType.FORM_URLENCODED.getVal())) {
                requestBody = new RequestBody<>(parseQueryParams(sb.toString(), false));
            }
            else {
                requestBody = new RequestBody<>(sb.toString());
            }

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private Map<String, String> parseQueryParams(String path, boolean isRawPath) {
        Map<String, String> map = new HashMap<>();

        if (path == null || path.isBlank()) return map;

        int n = path.length();
        int i = 0;

        if (isRawPath) {
            while (i < n && path.charAt(i) != '?') i++;

            i++;
            if (i >= n - 2) return map;
        }

        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();

        while (i < n) {
            while (i < n && path.charAt(i) != '=') {
                key.append(path.charAt(i));
                i++;
            }
            i++;

            while (i < n && path.charAt(i) != '&') {
                val.append(path.charAt(i));
                i++;
            }
            i++;

            map.put(key.toString(), val.toString());
            key.setLength(0);
            val.setLength(0);
        }

        return map;
    }

    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }
}
