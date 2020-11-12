package doom.sample;

import doom.middleware.MiddlewareHandler;
import doom.http.Request;
import doom.http.Response;

public class LogMiddleware implements MiddlewareHandler {
    @Override
    public Response handle(Request req) {
        System.out.println("Middleware executed");
        return new Response("ok");
    }
}
