package doom.http;

import com.sun.net.httpserver.HttpExchange;
import doom.annotations.MiddleWare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    String basePath;
    Map<String, Route> routes;
    List<MiddleWare> middleWares;

    public Controller(String basePath) {
        this.basePath = basePath;
        this.routes = new HashMap<>();
        this.middleWares = new ArrayList<>();
    }

    public void process(HttpExchange exchange) throws IOException {
        Response response;
        Request request = new Request(exchange);
        Route route = getMatchingRoute(request.getPath(), request.getMethod());

        if (route != null) {
            response = route.processRequest(request);
        } else {
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

    public String getBasePath() {
        return basePath;
    }
}
