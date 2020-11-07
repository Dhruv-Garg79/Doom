package doom.http;

import com.sun.net.httpserver.HttpExchange;

public class Request {
    private String path;
    private HttpMethods method;
    private HttpExchange exchange;

    public Request(HttpExchange exchange) {
        this.exchange = exchange;

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

    public HttpExchange getExchange() {
        return exchange;
    }
}
