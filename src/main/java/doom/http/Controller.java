package doom.http;

import com.sun.net.httpserver.HttpExchange;
import doom.middleware.MiddlewareAdder;
import doom.middleware.MiddlewareHandler;
import doom.middleware.MiddlewareProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller implements MiddlewareAdder {
    String basePath;
    Map<String, Route> routes;
    MiddlewareProcessor middlewareProcessor;

    public Controller(String basePath) {
        this.basePath = basePath;
        this.routes = new HashMap<>();
        middlewareProcessor = new MiddlewareProcessor();
    }

    public void process(HttpExchange exchange) throws IOException {
        if (!middlewareProcessor.process(exchange))
            return;

        Response response;
        Request request = new Request(exchange);
        Route route = getMatchingRoute(request.getPath(), request.getMethod());

        if (route != null) {
            response = route.processRequest(request);
        } else {
            response = Response.notFound();
        }

        if (response != null)
            response.send(exchange);
    }

    public Route getMatchingRoute(String path, HttpMethods method) {
        return routes.get(method.toString() + path);
    }

    public void addRoute(Route route) {
        routes.put(route.getMethod() + route.getPath(), route);
    }

    @Override
    public void addMiddleware(MiddlewareHandler middlewareHandler){
        middlewareProcessor.addMiddleware(middlewareHandler);
    }

    public String getBasePath() {
        return basePath;
    }
}
