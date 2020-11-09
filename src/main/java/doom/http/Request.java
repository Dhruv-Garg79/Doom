package doom.http;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String path;
    private HttpMethods method;
    private HttpExchange exchange;

    private Map<String, String> queryParams;

    public Request(HttpExchange exchange){
        initialize(exchange, exchange.getRequestURI().getPath());
    }
    public Request(HttpExchange exchange, String path) {
        initialize(exchange, path);
    }

    public void initialize(HttpExchange exchange, String path) {
        this.exchange = exchange;

        this.path = path;
        method = HttpMethods.valueOf(exchange.getRequestMethod());

        queryParams = parsePathForQueries(path);
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

    public Map<String, String> parsePathForQueries(String path){
        Map<String, String> map = new HashMap<>();
        int n = path.length();
        int i = 0;
        while (i < n && path.charAt(i) != '?')
            i++;

        i++;
        if (i >= n - 2)
            return map;

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
