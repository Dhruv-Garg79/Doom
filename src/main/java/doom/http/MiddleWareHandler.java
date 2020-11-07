package doom.http;

public interface MiddleWareHandler {
    public Response handle(Request req);
}
