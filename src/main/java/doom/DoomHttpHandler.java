package doom;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import doom.http.HttpMethods;
import doom.http.Request;
import doom.http.Response;
import doom.http.Route;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class DoomHttpHandler implements HttpHandler {
    Map<String, Route> routes;

    DoomHttpHandler(){
        this.routes = new HashMap<>();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Response response;
        Request request = new Request(exchange);
        Route route = getMatchingRoute(request.getPath(), request.getMethod());

        if (route != null){
            response = route.processRequest(request);
        }
        else{
            response = Response.notFound();
        }

        response.send(exchange);
    }

    public Route getMatchingRoute(String path, HttpMethods method){
        return routes.get(method.toString() + path);
    }

    public void addRoute(Route route){
        routes.put(route.getMethod() + route.getPath(), route);
    }
}
