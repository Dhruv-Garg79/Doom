package doom.http;

import com.sun.net.httpserver.HttpExchange;
import doom.middleware.MiddlewareAdder;
import doom.middleware.MiddlewareHandler;
import doom.middleware.MiddlewareProcessor;
import doom.models.Request;
import doom.models.Response;
import doom.utils.PathUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Controller implements MiddlewareAdder {
    private final String basePath;
    List<Route> routes;
    MiddlewareProcessor middlewareProcessor;

    public Controller(String basePath) {
        this.basePath = basePath;
        this.routes = new ArrayList<>();
        middlewareProcessor = new MiddlewareProcessor();
    }

    public void process(Request request, String routePath) throws IOException {
        if (!middlewareProcessor.process(request))
            return;

        HttpExchange exchange = request.getExchange();

        Response response = null;
        Route route = getMatchingRoute(routePath, request);

        if (route != null) {
            response = route.processRequest(request);
        }
        else{
            response = Response.notFound();
        }

        if (response != null)
            response.send(exchange);
    }

    public Route getMatchingRoute(String path, Request request) {
        Route res = null;
        Matcher matcher;

        for (Route route : routes){
            matcher = route.matchPath(path);
            if (route.getMethod().equals(request.getMethod()) && matcher.matches()){
                Map<String, String> pathParams = PathUtils.extractPathParams(matcher, route.getPath());
                request.setPathParams(pathParams);

                res = route;
                break;
            }
        }

        return res;
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    @Override
    public void addMiddleware(MiddlewareHandler middlewareHandler){
        middlewareProcessor.addMiddleware(middlewareHandler);
    }

    public String getBasePath() {
        return basePath;
    }
}
