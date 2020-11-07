package doom.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import doom.http.*;
import doom.middleware.MiddlewareAdder;
import doom.middleware.MiddlewareHandler;
import doom.middleware.MiddlewareProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class DoomHttpHandler implements HttpHandler, MiddlewareAdder {
    Map<String, Controller> controllers;
    MiddlewareProcessor globalMiddlewareProcessor;

    DoomHttpHandler() {
        this.controllers = new HashMap<>();
        globalMiddlewareProcessor = new MiddlewareProcessor();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!globalMiddlewareProcessor.process(exchange))
            return;

        Controller controller = getMatchingController(exchange.getRequestURI().getPath());
        controller.process(exchange);
    }

    public Controller getMatchingController(String path) {
        return controllers.get(path);
    }

    @Override
    public void addMiddleware(MiddlewareHandler middlewareHandler){
        globalMiddlewareProcessor.addMiddleware(middlewareHandler);
    }

    public void addController(Controller controller) {
        controllers.put(controller.getBasePath(), controller);
    }
}
