package doom.http;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String path;
    private HttpMethods method;
    private HttpExchange exchange;

    private Map<String, String> queryParams;
    public Request(HttpExchange exchange) {
        this.exchange = exchange;

        this.path = exchange.getRequestURI().getPath();
        method = HttpMethods.valueOf(exchange.getRequestMethod());

        queryParams = parseQuery(exchange.getRequestURI().getQuery(), false);
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

    public String getQuery(String key){
        return queryParams.get(key);
    }

    public Map<String, String> parseQuery(String path, boolean isRawPath){
        Map<String, String> map = new HashMap<>();

        int n = path.length();
        int i = 0;

        if (isRawPath) {
            while (i < n && path.charAt(i) != '?') i++;

            i++;
            if (i >= n - 2) return map;
        }

        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();

        while (i < n){
            while (i < n && path.charAt(i) != '=') {
                key.append(path.charAt(i));
                i++;
            }
            i++;

            while (i < n && path.charAt(i) != '&'){
                val.append(path.charAt(i));
                i++;
            }
            i++;

            map.put(key.toString(), val.toString());
            key = new StringBuilder();
            val = new StringBuilder();
        }

        return map;
    }
}
