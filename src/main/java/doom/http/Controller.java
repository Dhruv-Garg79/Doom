package doom.http;

import com.sun.net.httpserver.HttpExchange;
import doom.middleware.MiddlewareAdder;
import doom.middleware.MiddlewareHandler;
import doom.middleware.MiddlewareProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller implements MiddlewareAdder {
    private final String basePath;
    Map<String, Route> routes;
    MiddlewareProcessor middlewareProcessor;

    public Controller(String basePath) {
        this.basePath = basePath;
        this.routes = new HashMap<>();
        middlewareProcessor = new MiddlewareProcessor();
    }

    public void process(HttpExchange exchange, String routePath) throws IOException {
        if (!middlewareProcessor.process(exchange))
            return;

        Response response = null;
        Request request = new Request(exchange);
        Route route = getMatchingRoute(routePath, request.getMethod());

        if (route != null) {
            response = route.processRequest(request);
        }

        if (response == null){
            response = Response.notFound();
        }

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
