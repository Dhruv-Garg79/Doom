package doom;

import doom.middleware.MiddlewareHandler;
import doom.http.Request;
import doom.http.Response;

public class LogMiddleware implements MiddlewareHandler {
    @Override
    public Response handle(Request req) {
        System.out.println(req.toString());
        return new Response("ok");
    }
}
