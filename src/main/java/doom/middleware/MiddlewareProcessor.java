package doom.middleware;

import com.sun.net.httpserver.HttpExchange;
import doom.http.Request;
import doom.http.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MiddlewareProcessor {
    private final List<MiddlewareHandler> middlewareHandlers;

    public MiddlewareProcessor(){
        middlewareHandlers = new ArrayList<>();
    }

    public void addMiddleware(MiddlewareHandler middleware){
        middlewareHandlers.add(middleware);
    }

    public boolean process(HttpExchange exchange) throws IOException {
        Response response = null;
        Request request = new Request(exchange);

        for (MiddlewareHandler middleware : middlewareHandlers) {
            response = middleware.handle(request);
            if (response.getStatusCode() != 200) {
                response.send(exchange);
                return false;
            }
        }

        return true;
    }
}
