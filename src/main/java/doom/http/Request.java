package doom.http;

import com.sun.net.httpserver.HttpExchange;

public class Request {
    private String path;
    private HttpMethods method;

    public Request(HttpExchange exchange) {
        path = exchange.getRequestURI().getPath();
        method = HttpMethods.valueOf(exchange.getRequestMethod());
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
}
