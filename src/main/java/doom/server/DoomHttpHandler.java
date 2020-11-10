package doom.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import doom.http.Controller;
import doom.http.Response;
import doom.middleware.MiddlewareAdder;
import doom.middleware.MiddlewareHandler;
import doom.middleware.MiddlewareProcessor;
import doom.utils.Tuple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DoomHttpHandler implements HttpHandler, MiddlewareAdder {
    List<Controller> controllers;
    MiddlewareProcessor globalMiddlewareProcessor;

    DoomHttpHandler() {
        this.controllers = new ArrayList<>();
        globalMiddlewareProcessor = new MiddlewareProcessor();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!globalMiddlewareProcessor.process(exchange))
            return;

        Tuple<Controller, String> tuple = getMatchingController(exchange.getRequestURI().getPath());
        Controller controller = tuple.first;
        String path = tuple.second;

        if (controller != null) {
            controller.process(exchange, !path.equals("") ? path : "/");
        } else {
            Response.notFound().send(exchange);
        }
    }

    public Tuple<Controller, String> getMatchingController(String path) {
        Controller controller = null;
        int mostAccuratePath = 0;

        System.out.println(controllers.size());
        for (Controller ctrl : controllers){
            int i = longestSubstring(path, ctrl.getBasePath());
            if (i > 0 && i > mostAccuratePath && (i == path.length() || path.charAt(i) == '/')){
                mostAccuratePath = i;
                controller = ctrl;
            }
        }

        if (controller == null)
            return null;

        return new Tuple<>(controller, path.substring(mostAccuratePath));
    }

    public int longestSubstring(String x, String y){
        int i = 0;
        while (i < x.length() && i < y.length()){
            if (x.charAt(i) == y.charAt(i))
                i++;
            else
                break;
        }

        System.out.println(x + " " + y + " " + x.charAt(i));
        return i;
    }

    @Override
    public void addMiddleware(MiddlewareHandler middlewareHandler){
        globalMiddlewareProcessor.addMiddleware(middlewareHandler);
    }

    public void addController(Controller controller) {
        controllers.add(controller);
    }
}
