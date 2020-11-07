package doom;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import doom.http.Controller;
import doom.http.MiddleWareHandler;
import doom.http.Request;
import doom.http.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DoomHttpHandler implements HttpHandler {
    Map<String, Controller> controllers;
    List<MiddleWareHandler> globalMiddleWares;

    DoomHttpHandler() {
        this.controllers = new HashMap<>();
        this.globalMiddleWares = new ArrayList<>();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Response response = null;
        Request request = new Request(exchange);
        Controller controller = getMatchingController(exchange.getRequestURI().getPath());

        for (MiddleWareHandler middleware : globalMiddleWares) {
            response = middleware.handle(request);
            if (response.getStatusCode() != 200) {
                response.send(exchange);
                return;
            }
        }

        controller.process(exchange);
    }

    public Controller getMatchingController(String path) {
        return controllers.get(path);
    }

    public void addController(Controller controller) {
        controllers.put(controller.getBasePath(), controller);
    }

    public void addMiddleware(MiddleWareHandler middleWareHandler) {
        globalMiddleWares.add(middleWareHandler);
    }
}
