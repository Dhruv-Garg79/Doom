package doom;

import doom.http.MiddleWareHandler;
import doom.http.Request;
import doom.http.Response;

public class LogMiddleWare implements MiddleWareHandler {
    @Override
    public Response handle(Request req) {
        System.out.println(req.toString());
        return new Response("ok");
    }
}
