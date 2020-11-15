package doom.middleware;

import doom.models.Request;
import doom.models.Response;

public interface MiddlewareHandler {
    public Response handle(Request req);
}
