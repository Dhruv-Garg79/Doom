package doom.middleware;

import doom.http.Request;
import doom.http.Response;

public interface MiddlewareHandler {
    public Response handle(Request req);
}
